package generators.kotlin

import ce.io.CodeWritter
import ce.io.FileCodeWritter
import generators.obj.Writter
import generators.obj.input.*
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

class KotlinWritter(fileGenerator: KotlinFileGenerator, outputFolder: String)
    : Writter(fileGenerator, fileGenerator.style, outputFolder) {

    override fun writeFile(fileData: FileData) {
        var outputFile = File(fileData.name + ".kt")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(fileGenerator.newLine())
            writeNode(fileData, codeWritter, "")
        }
    }

    override fun writeNode(node: Node, out: CodeWritter, indent: String) {
        when (node) {
            is Method -> {
                out.write(node.name).write("(").setIndent(indent + fileGenerator.tabSpace)
                node.findOrNull(InputList::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.subs.remove(this)
                }
                out.setIndent(indent).write(")")
                node.findOrNull(ResultLeaf::class.java)?.apply {
                    writeLeaf(this, out, indent)
                    node.subs.remove(this)
                }
                out.writeNl()
            }
            is OutBlockArguments -> {
                out.write("(")
                out.setIndent(indent + fileGenerator.tabSpace)
                writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                out.setIndent(indent).writeNlIfNotEmpty().write(")")
            }
            is OutBlock -> {
                out.write(node.name)
                node.findOrNull(OutBlockArguments::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.subs.remove(this)
                }
                if (!(node.subs.isEmpty() && codeStyle.preventEmptyBlocks)) {
                    // prevent empty blocks
                    out.write(" {")
                    out.setIndent(indent + fileGenerator.tabSpace)
                    out.writeNl()
                    writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                    out.setIndent(indent).writeNlIfNotEmpty().write("}")
                }
                out.writeNl()
            }
            is ImportsBlock -> {
                if (node.subs.size > 0) {
                    out.writeNl()
                    writeSubNodes(node, out, indent)
                    out.writeNl()
                }
            }
            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeLeaf(leaf: Leaf, out: CodeWritter, indent: String) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.name}").writeNl()
            is NamespaceDeclaration -> out.write("package ${leaf.name}").writeNl()
            else -> super.writeLeaf(leaf, out, indent)
        }
    }
}