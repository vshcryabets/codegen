package generators.kotlin

import ce.formatters.CodeStyleRepo
import ce.io.CodeWriter
import ce.io.FileCodeWritter
import generators.obj.Writter
import generators.obj.abstractSyntaxTree.InputList
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Method
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.abstractSyntaxTree.findParent
import generators.obj.abstractSyntaxTree.removeSub
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.ImportLeaf
import generators.obj.syntaxParseTree.NamespaceDeclaration
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.ResultLeaf
import generators.obj.syntaxParseTree.FileMetaInformation
import generators.obj.syntaxParseTree.PackageDirectory
import generators.obj.syntaxParseTree.WorkingDirectory
import java.io.File

class KotlinWriter(codeStyleRepo: CodeStyleRepo, outputFolder: String)
    : Writter(codeStyleRepo, outputFolder) {
    override fun getFilePath(fileData: FileData): String {
        val fileMetaInformation = fileData.findOrNull(FileMetaInformation::class.java) ?:
            throw IllegalStateException("No working directory found in fileData ${fileData.name}")
        val workingDirectory = fileMetaInformation.findOrNull(WorkingDirectory::class.java)?.name ?:
            throw IllegalStateException("No working directory found in fileData ${fileData.name}")
        val packageDirectory = fileMetaInformation.findOrNull(PackageDirectory::class.java)?.name ?:
            throw IllegalStateException("No working directory found in fileData ${fileData.name}")
        return workingDirectory + File.separator +
                packageDirectory + File.separator +
                fileData.name + ".kt"
    }

    override fun writeFile(fileData: FileData) {
        val outputFile = File(getFilePath(fileData))
        outputFile.parentFile.mkdirs()
        println("KotlinWriter writing ${outputFile.absolutePath}")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeNode(fileData, codeWritter, "")
        }
    }

    override fun writeNode(node: Node, out: CodeWriter, indent: String) {
        when (node) {
            is Method -> {
                out.write(node.name).write("(").setIndent(indent + codeStyleRepo.tab)
                node.findOrNull(InputList::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.removeSub(this)
                }
                out.setIndent(indent).write(")")
                node.findOrNull(ResultLeaf::class.java)?.apply {
                    writeLeaf(this, out, indent)
                    node.removeSub(this)
                }
                out.writeNl()
            }
            is OutBlock -> {
                out.write(node.name)
                super.writeNode(node, out, indent)
            }
            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeLeaf(leaf: Leaf, out: CodeWriter, indent: String) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.name}")
            is NamespaceDeclaration -> out.write("package ${leaf.name}")
            else -> super.writeLeaf(leaf, out, indent)
        }
    }
}