package generators.cpp

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.FileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import java.io.File

class CppFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(outputFile: String): FileData {
        return CppFileData().apply {
            this.fullOutputFileName= outputFile
            this.headerBegin.append("#pragma once")
                .append(newLine())
        }
    }

    override fun appendInitalComment(s: FileData, s1: String) {
        super.appendInitalComment(s, s1)
//        (s as CppFileData)
    }
}