package generators.cpp

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput

class CppFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(project: ProjectOutput, outputFile: String, block: Block): FileData {
        return CppFileData(outputFile, project).apply {
            this.headerBegin.append("#pragma once")
                .append(newLine())
        }
    }

    override fun appendInitialComment(s: FileData, s1: String) {
        super.appendInitialComment(s, s1)
//        (s as CppFileData)
    }
}