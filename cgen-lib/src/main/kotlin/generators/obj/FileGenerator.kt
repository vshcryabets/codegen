package generators.obj

import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput

abstract class FileGenerator() {
    abstract fun createFile(project: ProjectOutput, outputFile: String, block: Block): List<FileData>

    abstract fun getBlockFilePath(block: Block): String
}