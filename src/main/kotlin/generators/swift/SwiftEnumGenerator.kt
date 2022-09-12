package generators.swift

import ce.defs.DataType
import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class SwiftEnumGenerator(
    fileGenerator: SwiftFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, SwiftClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsEnum): SwiftClassData {
        val result = super.processBlock(file, desc)
        result.apply {
            classComment.append(desc.classComment).append(fileGenerator.newLine())
            val withRawValues = desc.defaultDataType != DataType.VOID
            if (withRawValues) {
                appendClassDefinition(result, "enum ${desc.name}  : ${Types.typeTo(file, desc.defaultDataType)} {")
            } else {
                appendClassDefinition(result, "enum ${desc.name} {");
                putTabs(classDefinition, 1)
                classDefinition.append("case ")
            }
            var previous: Any? = null
            var needToAddComa = false
            desc.constants.forEach {
                if (it.value == null && previous != null) {
                    it.value = previous!! as Int + 1;
                }

                if (it.value != null) {
                    previous = it.value
                }

                if (withRawValues) {
                    putTabs(classDefinition, 1)
                    classDefinition.append("case ")
                        .append(it.name)
                        .append(" = ${Types.toValue(this, it.type, it.value)}")
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

    override fun createClassData(namespace: String): SwiftClassData = SwiftClassData(namespace)
}