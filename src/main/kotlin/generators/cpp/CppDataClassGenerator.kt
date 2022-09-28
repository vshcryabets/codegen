package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.DataClass
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData

class CppDataClassGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<DataClass, CppClassData>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: DataClass): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

//        val result = CppClassData(desc.name, parent)
        return header.addSub(CppClassData(desc.name, header)).apply {
            addSub(CommentsBlock(this)).apply {
                if (desc.classComment.isNotEmpty())
                    addSub(CommentLeaf(desc.classComment.toString(), this))
                addSub(CommentLeaf("Constants ${desc.name}", this))
            }

            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                classDefinition.append("const ")
                    .append(Types.typeTo(header, it.type))
                    .append(" ")
                    .append(it.name)
                    .append(" = ${Types.toValue(header, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
    }
}