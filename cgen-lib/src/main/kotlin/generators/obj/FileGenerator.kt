package generators.obj

import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.OutputTree

abstract class FileGenerator() {
    abstract fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData>

    abstract fun getBlockFilePath(block: Block): String
}