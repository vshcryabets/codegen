package generators.cpp

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput

class CppFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: ProjectOutput, outputFile: String, block: Block): List<FileData> {
        return listOf(
            CppFileData(outputFile + ".cpp").apply { setParent2(project) },
            CppHeaderFile(outputFile + ".h").apply { setParent2(project) }
        )
    }
}