package generators.java

import ce.formatters.CodeStyleRepo
import ce.io.CodeWriter
import ce.io.FileCodeWritter
import ce.repository.ReportsRepo
import generators.obj.Writter
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.ImportLeaf
import generators.obj.syntaxParseTree.NamespaceDeclaration
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.FileMetaInformation
import java.io.File

class JavaWritter(
    codeStyleRepo: CodeStyleRepo,
    outputFolder: String,
    private val reportsRepo: ReportsRepo)
    : Writter(codeStyleRepo, outputFolder) {

    override fun writeFile(fileData: FileData) {
        val fileMetaInformation = fileData.findOrNull(FileMetaInformation::class.java)?.name ?:
            throw IllegalStateException("No working directory found in fileData ${fileData.name}")
        val outputFile = File(fileMetaInformation + "/" + fileData.name + ".java")
        outputFile.parentFile.mkdirs()
        reportsRepo.logi("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeNode(fileData, codeWritter, "")
        }
    }

    override fun writeNode(node: Node, out: CodeWriter, indent: String) {
        when (node) {
            is OutBlock -> {
                out.write(node.name)
                super.writeNode(node, out, indent)
            }
            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeLeaf(leaf: Leaf, out: CodeWriter, indent: String) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.name}").writeNl()
            is NamespaceDeclaration -> out.write("package ${leaf.name};").writeNl()
            else -> super.writeLeaf(leaf, out, indent)
        }
    }
}