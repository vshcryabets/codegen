package generators.kotlin

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
            writeNode(fileData, out, "")
        }
    }

    override fun writeNode(node: Node, out: BufferedWriter, indent: String) {
        when (node) {
            is Method -> {
                out.write(indent)
                out.write(node.name)
                out.write("(")
                node.findOrNull(InputList::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.subs.remove(this)
                }
                out.write(")")
                node.findOrNull(ResultLeaf::class.java)?.apply {
                    writeLeaf(this, out, indent)
                    node.subs.remove(this)
                }
                out.write(fileGenerator.newLine())
            }
            is OutBlockArguments -> {
                out.write("(")
                writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                out.write(fileGenerator.newLine())
                out.write(indent)
                out.write(")")
            }
            is OutBlock -> {
                out.write(indent)
                out.write(node.name)
                node.findOrNull(OutBlockArguments::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.subs.remove(this)
                }
                if (!(node.subs.isEmpty() && codeStyle.preventEmptyBlocks)) {
                    // prevent empty blocks
                    out.write(" {")
                    writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                    out.write(fileGenerator.newLine())
                    out.write(indent)
                    out.write("}")
                }
                out.write(fileGenerator.newLine())
            }
            is ImportsBlock -> {
                if (node.subs.size > 0) {
                    out.write(fileGenerator.newLine())
                    writeSubNodes(node, out, indent)
                    out.write(fileGenerator.newLine())
                }
            }
            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeLeaf(leaf: Leaf, out: BufferedWriter, indent: String) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.name}${fileGenerator.newLine()}")
            is NamespaceDeclaration -> out.write("package ${leaf.name}${fileGenerator.newLine()}")
            else -> super.writeLeaf(leaf, out, indent)
        }
    }
}