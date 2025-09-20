package generators.rust

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.FileData

class RsConstantsBlockGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Rust")

        file.addSub(RustClassData(desc.name)).apply {
//            desc.classComment.append("Constants ${desc.name}")
//            addMultilineCommentsBlock(desc.classComment.toString(), this)
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
//                classDefinition.append("const ")
//                    .append(it.name)
//                    .append(" : ")
//                    .append(Types.typeTo(file, it.type))
//                    .append(" = ${Types.toValue(this, it.type, it.value)};")
//                    .append(fileGenerator.newLine())
            }
        }
    }
}