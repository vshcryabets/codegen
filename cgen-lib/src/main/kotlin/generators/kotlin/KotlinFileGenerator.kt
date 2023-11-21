package generators.kotlin

import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.input.addSub
import generators.obj.input.getParentPath
import generators.obj.out.*
import java.io.File

class KotlinFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
            addSub(NamespaceDeclaration(block.getParentPath()))
            addSub(ImportsBlock())
        })
    }

    override fun getBlockFilePath(block: Block): String {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
        val namespace = block.getParentPath().replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
    }
}