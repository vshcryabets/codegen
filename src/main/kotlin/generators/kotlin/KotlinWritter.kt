package generators.kotlin

import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Node
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
            writeNode(fileData, out)

            if (fileData.end.isNotEmpty()) {
                out.write(fileData.end.toString())
            }
        }
    }

    override fun writeNode(node: Node, out: BufferedWriter) {
        when (node) {
            is OutBlock -> {
                out.write(node.name)
                out.write(" {")
                out.write(fileGenerator.newLine())
                writeSubNodes(node, out)
                out.write("}")
                out.write(fileGenerator.newLine())
            }
//            is KotlinClassData -> {
//                super.writeNode(node, out)
//                if (node.classDefinition.isNotEmpty()) {
//                    out.write(node.classDefinition.toString())
//                }
//            }
            else -> super.writeNode(node, out)
        }
    }

    override fun writeLeaf(leaf: Leaf, out: BufferedWriter) {
        when (leaf) {
            is ConstantLeaf -> out.write("${leaf.name}${fileGenerator.newLine()}")
            is ImportLeaf -> out.write("import ${leaf.name}${fileGenerator.newLine()}")
            is NamespaceDeclaration -> out.write("package ${leaf.name}${fileGenerator.newLine()}")
            else -> super.writeLeaf(leaf, out)
        }
    }
}