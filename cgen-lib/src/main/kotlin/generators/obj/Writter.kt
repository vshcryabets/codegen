package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.DataField
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

abstract class Writter(val fileGenerator: FileGenerator,
                       val codeStyle: CodeStyle,
                       outputFolderPath: String) {
    val outFolder : File

    init {
        outFolder = File(outputFolderPath)
        outFolder.mkdirs()
    }

    open fun write(data: ProjectOutput) {
        data.subs.forEach {
            if (it is FileData) {
                writeFile(it)
            }
        }
    }

    abstract fun writeFile(fileData: FileData)

    open fun writeLeaf(leaf: Leaf, out: BufferedWriter, indent: String) {
        when (leaf) {
            is ArgumentLeaf, is ResultLeaf, is Separator -> {
                out.write(leaf.name)
            }
            is RValue -> out.write(leaf.name)
            is DataField -> out.write(leaf.name)
            is Keyword -> out.write(leaf.name + " ")
            is VariableName -> out.write(leaf.name)
            is EnumLeaf -> {
                out.write(indent)
                out.write(leaf.name)
            }
            is FieldLeaf, is CommentLeaf -> {
                out.write(indent)
                out.write(leaf.name)
                writeNewLine(out, "")
            }
            is NlSeparator -> {
                out.write(leaf.name)
                writeNewLine(out, "")
            }
            is BlockPreNewLines -> {
                for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
            }
            else -> out.write("=== UNKNOWN LEAF $leaf ${fileGenerator.newLine()}")
        }
    }

    open fun writeNewLine(out: BufferedWriter, indent: String) {
        out.write(fileGenerator.newLine())
        out.write(indent)
    }

    open fun writeSubNodes(node: Node, out: BufferedWriter, indent: String) {
        node.subs.forEach {
            if (it is Node) {
                writeNode(it, out, indent)
            } else {
                writeLeaf(it, out, indent)
            }
        }
    }

    open fun writeNode(node: Node, out: BufferedWriter, indent: String) {
        when (node) {
            is ConstantLeaf -> writeSubNodes(node, out, "")
            is ClassData -> writeSubNodes(node, out, indent)
            is MultilineCommentsBlock -> {
                out.write(indent)
                out.write(fileGenerator.multilineCommentStart())
                writeSubNodes(node, out, "$indent ")
                out.write("$indent ")
                out.write(fileGenerator.multilineCommentEnd())
            }
            is CommentsBlock -> {
                writeSubNodes(node, out, indent)
            }
            else -> writeSubNodes(node, out, indent)
        }

    }
}