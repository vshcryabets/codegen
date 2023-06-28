package generators.cpp

import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Namespace
import generators.obj.input.Node
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

class CppWritter(fileGenerator: FileGenerator, outputFolder: String)
    : Writter(fileGenerator, fileGenerator.style, outputFolder) {

    override fun writeLeaf(leaf: Leaf, out: BufferedWriter, indent: String) {
        when (leaf) {
            is CompilerDirective -> out.write("#${leaf.name}${fileGenerator.newLine()}")
            is ImportLeaf -> out.write("#include \"${leaf.name}\"${fileGenerator.newLine()}")
            else -> super.writeLeaf(leaf, out, indent)
        }
    }

    override fun writeNode(node: Node, out: BufferedWriter, indent: String) {
        when (node) {
            is NamespaceBlock -> {
                out.write("namespace ${node.name.replace(".", "::")} {${fileGenerator.newLine()}")
                super.writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                out.write("}${fileGenerator.newLine()}")
            }
            is OutBlock -> {
                out.write(indent)
                out.write(node.name)
                node.findOrNull(OutBlockArguments::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.subs.remove(this)
                }
                out.write(" {")
                writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                out.write(fileGenerator.newLine())
                out.write(indent)
                out.write("};")
                out.write(fileGenerator.newLine())
            }
            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeFile(fileData: FileData) {
        val outputFile = File(fileData.name)
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            writeSubNodes(fileData, out, "")
        }
    }
}