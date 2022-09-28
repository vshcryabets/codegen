package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

abstract class Writter(val fileGenerator: FileGenerator, val codeStyle: CodeStyle, outputFolderPath: String) {
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

    open fun writeLeaf(leaf: Leaf, out: BufferedWriter) {
        when (leaf) {
            is BlockStart, is BlockEnd, is FieldLeaf -> {
                out.write(leaf.name)
                out.write(fileGenerator.newLine())
            }
            is CommentLeaf -> {
                out.write(leaf.name)
                out.write(fileGenerator.newLine())
            }
            is BlockPreNewLines -> {
                for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
            }
            else -> out.write("=== UNKNOWN LEAF $leaf ${fileGenerator.newLine()}")
        }
    }

    open fun writeSubNodes(node: Node, out: BufferedWriter) {
        node.subs.forEach {
            if (it is Node) {
                writeNode(it, out)
            } else {
                writeLeaf(it, out)
            }
        }
    }

    open fun writeNode(node: Node, out: BufferedWriter) {
        when (node) {
            is MultilineCommentsBlock -> {
                out.write(fileGenerator.multilineCommentStart())
                writeSubNodes(node, out)
                out.write(fileGenerator.multilineCommentEnd())
            }
            is CommentsBlock -> {
                writeSubNodes(node, out)
            }
            else -> writeSubNodes(node, out)
        }

    }

    fun writeNotEmpty(out: BufferedWriter, builder: StringBuilder) {
        if (builder.isNotEmpty()) {
            out.write(builder.toString())
        }
    }

}