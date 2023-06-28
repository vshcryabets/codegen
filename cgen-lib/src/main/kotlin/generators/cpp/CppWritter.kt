package generators.cpp

import ce.io.CodeWritter
import ce.io.FileCodeWritter
import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.*
import java.io.File

class CppWritter(fileGenerator: FileGenerator, outputFolder: String) :
    Writter(fileGenerator, fileGenerator.style, outputFolder) {

    override fun writeLeaf(leaf: Leaf, out: CodeWritter, indent: String) {
        when (leaf) {
            is CompilerDirective -> out.write("#${leaf.name}").writeNl()
            is ImportLeaf -> out.write("#include \"${leaf.name}\"").writeNl()
            else -> super.writeLeaf(leaf, out, indent)
        }
    }

    override fun writeNode(node: Node, out: CodeWritter, indent: String) {
        when (node) {
            is NamespaceBlock -> {
                out.write("namespace ${node.name.replace(".", "::")}")
                    .writeNl()
                super.writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                out.write("}").writeNl()
            }

            is OutBlock -> {
                out.write(indent)
                out.write(node.name)
                node.findOrNull(OutBlockArguments::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.subs.remove(this)
                }
                out.write(" {")
                out.setIndent(indent + fileGenerator.tabSpace)
                writeSubNodes(node, out, indent + fileGenerator.tabSpace)
                out.writeNl()
                out.write(indent)
                out.write("};")
                out.writeNl()
            }

            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeFile(fileData: FileData) {
        val outputFile = File(fileData.name)
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(fileGenerator.newLine())
            writeSubNodes(fileData, codeWritter, "")
        }
    }
}