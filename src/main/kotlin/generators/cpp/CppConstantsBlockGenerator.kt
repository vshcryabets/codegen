package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData
import javax.xml.stream.events.Namespace

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, desc: ConstantsBlock): CppClassData {
        val result = super.processBlock(file, desc)
        result.headerData.apply {
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

    override fun createClassData(namespace: String): CppClassData = CppClassData(namespace)
}