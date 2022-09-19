package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.FileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.nodes.FileInitialCommentsBlock
import java.io.File

class KotlinFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(outputFile: String): FileData {
        return FileData().apply {
            this.fullOutputFileName= outputFile
            addLeaf(FileInitialCommentsBlock())
            addLeaf(NamespaceDeclaration("MISSED"))
        }
    }

    override fun getBlockFilePath(block: Block): String {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
        val namespace = block.namespace.replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
    }
}