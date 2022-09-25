package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantsBlock
import generators.obj.input.Node
import generators.obj.out.FileData

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, parent: Node, desc: ConstantsBlock): CppClassData {
        val result = CppClassData(desc.name, parent)
//        result.headerData.apply {
//            appendNotEmptyWithNewLine(desc.classComment.toString(), classComment)
//            classComment
//                .append("Constants ${desc.name}")
//                .append(fileGenerator.newLine())
//
//            desc.subs.forEach { leaf ->
//                val it = leaf as ClassField
//                classDefinition.append("const ")
//                    .append(Types.typeTo(file, it.type))
//                    .append(" ")
//                    .append(it.name)
//                    .append(" = ${Types.toValue(this, it.type, it.value)};")
//                    .append(fileGenerator.newLine())
//            }
//        }
        return result
    }
}