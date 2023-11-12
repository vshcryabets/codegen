package generators.cpp

import ce.formatters.CodeStyleRepo
import ce.io.CodeWritter
import ce.io.FileCodeWritter
import ce.settings.CodeStyle
import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.findOrNull
import generators.obj.input.removeSub
import generators.obj.out.*
import java.io.File

class CppWritter(codeStyleRepo: CodeStyleRepo, outputFolder: String) :
    Writter(codeStyleRepo, outputFolder) {

    override fun writeLeaf(leaf: Leaf, out: CodeWritter, indent: String) {
        when (leaf) {
            is CompilerDirective -> out.write("#${leaf.name}")
            is ImportLeaf -> out.write("#include \"${leaf.name}\"")
            else -> super.writeLeaf(leaf, out, indent)
        }
    }

    override fun writeNode(node: Node, out: CodeWritter, indent: String) {
        when (node) {
            is NamespaceBlock -> {
                out.write("namespace ${node.name.replace(".", "::")}")
                super.writeSubNodes(node, out, indent + codeStyleRepo.tab)
            }

            is OutBlock -> {
                out.write(node.name)
                node.findOrNull(OutBlockArguments::class.java)?.apply {
                    writeNode(this, out, indent)
                    node.removeSub(this)
                }
                writeSubNodes(node, out, indent + codeStyleRepo.tab)
            }

            else -> super.writeNode(node, out, indent)
        }
    }

    override fun writeFile(fileData: FileData) {
        if (!fileData.isDirty) {
            println("No data to write ${fileData.name}")
            return
        }
        val outputFile = File(fileData.name)
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeSubNodes(fileData, codeWritter, "")
        }
    }
}