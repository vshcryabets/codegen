package generators.rust

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.FileMetaInformation
import java.io.File

class RustFileGenerator() : CLikeFileGenerator() {
    override fun createFile(
        project: OutputTree,
        workingDirectory: String,
        packageDirectory: String,
        outputFile: String,
        block: Block
    ): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
            addSub(FileMetaInformation(workingDirectory))
        })
    }

    override fun getBlockFilePath(block: Block): BlockPath {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
        val namespace = block.getParentPath().replace('.', File.separatorChar)
        return BlockPath(
            baseObjectDirecotry = block.objectBaseFolder,
            namespacePath = namespace,
            fileName = fileName
        )
    }
}