package generators.obj

import generators.obj.abstractSyntaxTree.Block
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.OutputTree

abstract class FileGenerator() {
    data class BlockPath(
        val baseObjectDirecotry: String = "",
        val namespacePath: String = "",
        val fileName: String =""
    )

    abstract fun createFile(
        project: OutputTree,
        workingDirectory: String,
        packageDirectory: String,
        outputFile: String,
        block: Block
    ): List<FileData>

    abstract fun getBlockFilePath(block: Block): BlockPath
}