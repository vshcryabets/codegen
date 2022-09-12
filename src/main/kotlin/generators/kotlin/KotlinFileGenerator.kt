package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.FileGenerator
import generators.obj.out.FileData

class KotlinFileGenerator(style: CodeStyle) : FileGenerator(style) {
    override fun createFile(outputFile: String): FileData {
        return FileData().apply {
            this.fullOutputFileName= outputFile
        }
    }

    override fun newLine(): String = "\n"
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentMid(): String = "*"
    override fun multilineCommentEnd(): String = "*/${newLine()}"
    override fun singleComment(): String = "//"
}