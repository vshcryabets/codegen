package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.input.Node
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData

class CppDataClassGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<DataClass, CppClassData>(fileGenerator) {

    override fun processBlock(file: FileData, parent: Node, desc: DataClass): CppClassData {
        val result = CppClassData(desc.name, parent)
        val headerData = CppHeaderData(desc.name, result)
        result.subs.add(headerData)
        headerData.apply {
            subs.add(CommentsBlock(this).apply {
                if (desc.classComment.isNotEmpty())
                    subs.add(CommentLeaf(desc.classComment.toString(), this))
                subs.add(CommentLeaf("Constants ${desc.name}", this))
            })

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