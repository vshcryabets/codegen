package generators.obj

import ce.settings.CodeStyle
import generators.obj.out.FileData
import generators.obj.out.OutLeaf
import generators.obj.out.OutNode
import generators.obj.out.ProjectOutput
import generators.obj.out.leafs.CommentLeaf
import generators.obj.out.leafs.ImportLeaf
import generators.obj.out.nodes.FileInitialCommentsBlock
import java.io.BufferedWriter
import java.io.File

abstract class Writter(val codeStyle: CodeStyle, outputFolderPath: String) {
    val outFolder : File

    init {
        outFolder = File(outputFolderPath)
        outFolder.mkdirs()
    }

    abstract fun write(data: ProjectOutput)
    abstract fun writeFile(fileData: FileData)

    open fun writeLeaf(leaf: OutLeaf, out: BufferedWriter) {
        if (leaf is CommentLeaf) {
            out.write(leaf.line)
        } else {
            out.write("=== UNKNOWN LEAF $leaf")
        }
    }

    fun writeNode(node: OutNode, out: BufferedWriter) {
        node.leafs.forEach {
            if (it is OutNode) {
                writeNode(it, out)
            } else {
                writeLeaf(it, out)
            }
        }
    }

    fun writeNotEmpty(out: BufferedWriter, builder: StringBuilder) {
        if (builder.isNotEmpty()) {
            out.write(builder.toString())
        }
    }

}