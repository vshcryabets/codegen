package generators.cpp

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput

class CppFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(project: ProjectOutput, outputFile: String, block: Block): List<FileData> {
        return listOf(
            CppFileData(outputFile + ".cpp", project),
            CppHeaderFile(outputFile + ".h", project)
        )
    }
}