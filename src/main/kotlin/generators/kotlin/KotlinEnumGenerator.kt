package generators.kotlin

import ce.defs.DataType
import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class KotlinEnumGenerator(
    fileGenerator: KotlinFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, KotlinClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsEnum): KotlinClassData {
        val result = super.processBlock(file, desc)
        result.apply {
            classComment.append(desc.classComment).append(fileGenerator.newLine())
            val withRawValues = desc.defaultDataType != DataType.VOID

            appendClassDefinition(result, "enum ${desc.name} {");
            var previous: Any? = null
            var needToAddComa = false
            if (!withRawValues) {
                putTabs(classDefinition, 1)
            }
            desc.constants.forEach {
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                if (withRawValues) {
                    putTabs(classDefinition, 1)
                    classDefinition
                        .append(it.name)
                        .append("(${Types.toValue(this, it.type, it.value)}),")
                        .append(fileGenerator.newLine())
                } else {
                    if (needToAddComa) {
                        classDefinition.append(", ")
                    }
                    classDefinition.append(it.name)
                    needToAddComa = true
                }
            }
            if (!withRawValues) {
                classDefinition.append(fileGenerator.newLine())
            }
            appendClassDefinition(result, "}");
        }
        return result
    }

    override fun createClassData(namespace: String): KotlinClassData = KotlinClassData(namespace)
}