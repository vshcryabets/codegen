package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.DataField
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

    open fun writeLeaf(leaf: Leaf, out: BufferedWriter, indent: String) {
        when (leaf) {
            is ArgumentLeaf, is ResultLeaf, is Separator -> {
                out.write(leaf.name)
            }
            is DataField -> {
                out.write(indent)
                out.write(leaf.name)
            }
            is ConstantLeaf -> {
                out.write(indent)
                out.write("${leaf.name}${fileGenerator.newLine()}")
            }
            is EnumLeaf -> {
                out.write(indent)
                out.write(leaf.name)
            }
            is FieldLeaf, is CommentLeaf -> {
                out.write(indent)
                out.write(leaf.name)
                out.write(fileGenerator.newLine())
            }
            is NlSeparator -> out.write(fileGenerator.newLine())
            is BlockPreNewLines -> {
                for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
            }
            else -> out.write("=== UNKNOWN LEAF $leaf ${fileGenerator.newLine()}")
        }
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
            is ClassData -> {
                writeSubNodes(node, out, indent)
//                if (node.classDefinition.isNotEmpty()) {
//                    out.write(node.classDefinition.toString())
//                }
            }
            is MultilineCommentsBlock -> {
                out.write(fileGenerator.multilineCommentStart())
                writeSubNodes(node, out, indent)
                out.write(fileGenerator.multilineCommentEnd())
            }
            is CommentsBlock -> {
                writeSubNodes(node, out, indent)
            }
            else -> writeSubNodes(node, out, indent)
        }

    }
}