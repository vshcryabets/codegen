package generators.cpp

import ce.settings.CodeStyle
import generators.obj.FileGenerator
import generators.obj.out.FileData

class CppFileGenerator(style: CodeStyle) : FileGenerator(style) {
    override fun createFile(namespace: String, outputFile: String): FileData {
        return CppFileData().apply {
            this.namespace = namespace
            this.fullOutputFileName= outputFile
            this.headerFile.namespace = namespace
            this.headerFile.fullOutputFileName = outputFile
        }
    }

    override fun newLine(): String = "\n"
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentEnd(): String = "*/${newLine()}"
    override fun singleComment(): String = "//"
    override fun commentStart(): String = "/*${newLine()}"
    override fun commentEnd(): String = "*/${newLine()}"
}