package generators.swift

import ce.defs.DataType
import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class SwiftEnumGenerator(
    fileGenerator: SwiftFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: ConstantsEnum): SwiftClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Swift")

        return file.addSub(SwiftClassData(desc.name, file)).apply {

            addMultilineCommentsBlock(desc.classComment.toString(), this)
            val withRawValues = desc.defaultDataType != DataType.VOID
            if (withRawValues) {
                appendClassDefinition(this, "enum ${desc.name}  `: ${Types.typeTo(file, desc.defaultDataType)} {")
            } else {
                appendClassDefinition(this, "enum ${desc.name} {");
                putTabs(classDefinition, 1)
                classDefinition.append("case ")
            }
            val autoIncrement = AutoincrementInt()
            var needToAddComa = false
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                autoIncrement(it)

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
            appendClassDefinition(this, "}");
        }
    }
}