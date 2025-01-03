package generators.java

import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.input.addSub
import generators.obj.input.getParentPath
import generators.obj.out.FileData
import generators.obj.out.FileDataImpl
import generators.obj.out.ImportsBlock
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.OutputTree
import java.io.File

class JavaFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
            addSub(NamespaceDeclaration(block.getParentPath()))
            addSub(ImportsBlock())
        })
    }

    override fun getBlockFilePath(block: Block): String {
        var fileName = block.name
        if (block.outputFile.isNotEmpty()) {
            fileName = block.outputFile
        }
        val namespace = block.getParentPath().replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
    }
}