package generators.kotlin

import ce.formatters.CodeStyleRepo
import ce.io.CodeWritter
import ce.io.FileCodeWritter
import ce.settings.CodeStyle
import generators.obj.Writter
import generators.obj.input.*
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

class KotlinWritter(codeStyleRepo: CodeStyleRepo, outputFolder: String)
    : Writter(codeStyleRepo, outputFolder) {

    override fun writeFile(fileData: FileData) {
        val outputFile = File(fileData.name + ".kt")
        outputFile.parentFile.mkdirs()
//        println("KotlinWritter fileData= $fileData")
        println("KotlinWritter writing ${outputFile.absolutePath}")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeNode(fileData, codeWritter, "")
        }
    }

    override fun writeNode(node: Node, out: CodeWritter, indent: String) {
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

    override fun writeLeaf(leaf: Leaf, out: CodeWritter, indent: String) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.name}")
            is NamespaceDeclaration -> out.write("package ${leaf.name}")
            else -> super.writeLeaf(leaf, out, indent)
        }
    }
}