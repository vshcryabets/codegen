package generators.java

import ce.formatters.CodeStyleRepo
import ce.io.CodeWriter
import ce.io.FileCodeWritter
import ce.repository.ReportsRepo
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.FileData
import generators.obj.out.ImportLeaf
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.OutBlock
import java.io.File

class JavaWritter(
    codeStyleRepo: CodeStyleRepo,
    outputFolder: String,
    private val reportsRepo: ReportsRepo)
    : Writter(codeStyleRepo, outputFolder) {

    override fun writeFile(fileData: FileData) {
        var outputFile = File(fileData.name + ".java")
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