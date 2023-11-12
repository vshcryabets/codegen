package generators.java

import ce.formatters.CodeStyleRepo
import ce.io.CodeWritter
import ce.io.FileCodeWritter
import ce.settings.CodeStyle
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

class JavaWritter(codeStyleRepo: CodeStyleRepo, outputFolder: String)
    : Writter(codeStyleRepo, outputFolder) {

    override fun writeFile(fileData: FileData) {
        var outputFile = File(fileData.name + ".java")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeNode(fileData, codeWritter, "")
        }
    }

    override fun writeNode(node: Node, out: CodeWritter, indent: String) {
        when (node) {
//            is KotlinClassData -> {
//                super.writeNode(node, out)
//                if (node.classDefinition.isNotEmpty()) {
//                    out.write(node.classDefinition.toString())
//                }
//            }
            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeLeaf(leaf: Leaf, out: CodeWritter, indent: String) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.name}").writeNl()
            is NamespaceDeclaration -> out.write("package ${leaf.name};").writeNl()
            else -> super.writeLeaf(leaf, out, indent)
        }
    }
}