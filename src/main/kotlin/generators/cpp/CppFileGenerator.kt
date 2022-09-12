package generators.cpp

import ce.settings.CodeStyle
import generators.obj.FileGenerator
import generators.obj.out.FileData

class CppFileGenerator(style: CodeStyle) : FileGenerator(style) {
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

    override fun newLine(): String = "\n"
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentEnd(): String = "*/${newLine()}"
    override fun singleComment(): String = "//"
}