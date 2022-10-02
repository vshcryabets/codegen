package generators.cpp

import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Namespace
import generators.obj.input.Node
import generators.obj.out.FileData
import generators.obj.out.ImportLeaf
import java.io.BufferedWriter
import java.io.File

class CppWritter(fileGenerator: FileGenerator, outputFolder: String)
    : Writter(fileGenerator, fileGenerator.style, outputFolder) {

    override fun writeLeaf(leaf: Leaf, out: BufferedWriter) {
        when (leaf) {
            is CompilerDirective -> out.write("#${leaf.name}${fileGenerator.newLine()}")
            is ImportLeaf -> out.write("#include \"${leaf.name}\"${fileGenerator.newLine()}")
            else -> super.writeLeaf(leaf, out)
        }
    }

    override fun writeNode(node: Node, out: BufferedWriter) {
        when (node) {
            is Namespace -> {
                out.write("namespace ${node.name} {${fileGenerator.newLine()}")
                super.writeNode(node, out)
                out.write("}${fileGenerator.newLine()}")
            }
            else -> super.writeNode(node, out)
        }
    }

    override fun writeFile(fileData: FileData) {
        val outputFile = File(fileData.name)
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            writeSubNodes(fileData, out)
        }
    }
}