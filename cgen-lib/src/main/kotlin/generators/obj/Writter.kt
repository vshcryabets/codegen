package generators.obj

import ce.formatters.CodeStyleRepo
import ce.io.CodeWritter
import ce.settings.CodeStyle
import generators.obj.input.DataField
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.*
import java.io.File

abstract class Writter(val codeStyleRepo: CodeStyleRepo,
                       outputFolderPath: String) {
    val outFolder : File

    init {
        outFolder = File(outputFolderPath)
        outFolder.mkdirs()

        val builder = StringBuilder()
    }

    open fun write(data: ProjectOutput) {
        data.subs.forEach {
            if (it is FileData) {
                writeFile(it)
            }
        }
    }

    abstract fun writeFile(fileData: FileData)

    open fun writeLeaf(leaf: Leaf, out: CodeWritter, indent: String) {
        when (leaf) {
            is ArgumentLeaf, is ResultLeaf, is Separator -> {
                out.write(leaf.name)
            }
            is RValue -> out.write(leaf.name)
            is DataField -> out.write(leaf.name)
            is Keyword -> out.write(leaf.name)
            is Datatype -> out.write(leaf.name)
            is VariableName -> out.write(leaf.name)
            is EnumLeaf -> {
                out.write(leaf.name)
            }
            is FieldLeaf, is CommentLeaf -> {
                out.write(leaf.name)
            }
            is Indent -> {
                out.write(codeStyleRepo.tab)
            }
            is NlSeparator -> {
                if (leaf.name.isEmpty()) {
                    out.write(codeStyleRepo.newLine())
                } else {
                    out.write(leaf.name)
                }
            }
            else -> out.write("=== UNKNOWN LEAF $leaf").writeNl()
        }
    }

    open fun writeSubNodes(node: Node, out: CodeWritter, indent: String) {
        node.subs.forEach {
            if (it is Node) {
                writeNode(it, out, indent)
            } else {
                writeLeaf(it, out, indent)
            }
        }
    }

    open fun writeNode(node: Node, out: CodeWritter, indent: String) {
        when (node) {
//            is ConstantLeaf -> writeSubNodes(node, out, "")
//            is ClassData -> writeSubNodes(node, out, indent)
//            is MultilineCommentsBlock -> {
//                out.write(indent)
//                out.write(codeStyleRepo.multilineCommentStart())
//                writeSubNodes(node, out, "$indent ")
//                out.write("$indent ")
//                out.write(codeStyleRepo.multilineCommentEnd())
//            }
//            is CommentsBlock -> {
//                writeSubNodes(node, out, indent)
//            }
            else -> writeSubNodes(node, out, indent)
        }

    }
}