package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.out.FileData

class CppDataClassGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<DataClass, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: DataClass): CppClassData {
        val result = CppClassData(desc.getParentPath(), desc.name, file)
        val headerData = CppHeaderData(desc.getParentPath(), desc.name, result)
        result.subs.add(headerData)
        headerData.apply {
            appendNotEmptyWithNewLine(desc.classComment.toString(), classComment)
            classComment
                .append("Constants ${desc.name}")
                .append(fileGenerator.newLine())

            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                classDefinition.append("const ")
                    .append(Types.typeTo(file, it.type))
                    .append(" ")
                    .append(it.name)
                    .append(" = ${Types.toValue(this, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
        return result
    }
}