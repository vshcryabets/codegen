package generators.obj

import generators.obj.abstractSyntaxTree.Block
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.OutputTree

abstract class FileGenerator() {
    abstract fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData>

    abstract fun getBlockFilePath(block: Block): String
}