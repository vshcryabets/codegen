package generators.cpp

import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.OutputTree

class CppFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData> {
        return listOf(
            CppFileData(outputFile + ".cpp").apply {
                setParent2(project)
            },
            CppHeaderFile(outputFile + ".h").apply {
                setParent2(project)
            }
        )
    }
}