package generators.kotlin

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

            appendClassDefinition(result, "object ${desc.name} {");
            var previous: Any? = null
            desc.constants.forEach {
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                putTabs(classDefinition, 1)
                classDefinition.append("const val ")
                    .append(it.name)
                    .append(" : ${Types.typeTo(file, it.type)}")
                    .append(" = ${Types.toValue(this, it.type, it.value)}")
                    .append(fileGenerator.newLine())
            }
            appendClassDefinition(result, "}");
        }
        return result
    }

    override fun createClassData(namespace: String): KotlinClassData = KotlinClassData(namespace)
}