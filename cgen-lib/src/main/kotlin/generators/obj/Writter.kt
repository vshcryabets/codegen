package generators.obj

import ce.defs.DataValue
import ce.defs.RValue
import ce.formatters.CodeStyleRepo
import ce.io.CodeWriter
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.syntaxParseTree.AstTypeLeaf
import generators.obj.syntaxParseTree.CodeStyleOutputTree
import generators.obj.syntaxParseTree.CommentLeaf
import generators.obj.syntaxParseTree.Constructor
import generators.obj.syntaxParseTree.EnumNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.Indent
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.ResultLeaf
import generators.obj.syntaxParseTree.Separator
import generators.obj.syntaxParseTree.Space
import generators.obj.syntaxParseTree.VariableName
import generators.obj.syntaxParseTree.FileMetaInformation
import java.io.File

abstract class Writter(val codeStyleRepo: CodeStyleRepo,
                       outputFolderPath: String) {
    val outFolder : File = File(outputFolderPath)

    init {
        outFolder.mkdirs()
    }

    open fun write(data: CodeStyleOutputTree) {
        data.subs.forEach {
            if (it is FileData) {
                writeFile(it)
            }
        }
    }

    abstract fun getFilePath(fileData: FileData): String
    abstract fun writeFile(fileData: FileData)

    open fun writeLeaf(leaf: Leaf, out: CodeWriter, indent: String) {
        when (leaf) {
            is ResultLeaf, is Separator -> {
                out.write(leaf.name)
            }
            is DataValue -> out.write(leaf.name)
            is DataField -> out.write(leaf.name)
            is Keyword -> out.write(leaf.name)
            is AstTypeLeaf -> out.write(leaf.name)
            is VariableName -> out.write(leaf.name)
            is CommentLeaf -> {
                out.write(leaf.name)
            }
            is Indent -> {
                out.write(codeStyleRepo.tab)
            }
            is Space -> {
                out.write(" ")
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

    open fun writeSubNodes(node: Node, out: CodeWriter, indent: String) {
        node.subs.forEach {
            if (it is Node) {
                writeNode(it, out, indent)
            } else {
                writeLeaf(it, out, indent)
            }
        }
    }

    open fun writeNode(node: Node, out: CodeWriter, indent: String) {
        when (node) {
            is FileMetaInformation -> {
                // do nothing, this is just meta info
            }
            is EnumNode, is RValue, is Constructor -> {
                out.write(node.name)
                writeSubNodes(node, out, indent)
            }
            else -> writeSubNodes(node, out, indent)
        }

    }
}