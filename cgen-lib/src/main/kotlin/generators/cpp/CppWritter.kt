package generators.cpp

import ce.formatters.CodeStyleRepo
import ce.io.CodeWriter
import ce.io.FileCodeWritter
import ce.repository.ReportsRepo
import generators.obj.Writter
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.abstractSyntaxTree.removeSub
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileMetaInformation
import generators.obj.syntaxParseTree.ImportLeaf
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import java.io.File

class CppWritter(
    codeStyleRepo: CodeStyleRepo, outputFolder: String,
    private val reportsRepo: ReportsRepo
) : Writter(codeStyleRepo, outputFolder) {

    override fun getFilePath(fileData: FileData): String {
        val fileMetaInformation = fileData.findOrNull(FileMetaInformation::class.java)?.name
            ?: throw IllegalStateException("No working directory found in fileData ${fileData.name}")
        return fileMetaInformation + "/" + fileData.name
    }

    override fun writeLeaf(leaf: Leaf, out: CodeWriter, indent: String) {
        when (leaf) {
            is CompilerDirective -> out.write("#${leaf.name}")
            is ImportLeaf -> {
                if (leaf.name.startsWith("<") && leaf.name.endsWith(">")) {
                    out.write("#include ${leaf.name}")
                } else if (leaf.name.startsWith("\"") && leaf.name.endsWith("\"")) {
                    out.write("#include ${leaf.name}")
                } else {
                    out.write("#include \"${leaf.name}\"")
                }
            }

            else -> super.writeLeaf(leaf, out, indent)
        }
    }

    override fun writeNode(node: Node, out: CodeWriter, indent: String) {
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
            reportsRepo.loge("No data to write ${fileData.name}")
            return
        }
        val outputFile = File(getFilePath(fileData))
        outputFile.parentFile.mkdirs()
        reportsRepo.logi("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeSubNodes(fileData, codeWritter, "")
        }
    }
}