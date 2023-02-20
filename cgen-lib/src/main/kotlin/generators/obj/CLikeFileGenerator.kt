package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.Block
import java.io.File

abstract class CLikeFileGenerator(codeStyle: CodeStyle) : FileGenerator(codeStyle) {

    override fun newLine(): String = "\n"
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentMid(): String = "*"
    override fun multilineCommentEnd(): String = "**/${newLine()}"
    override fun singleComment(): String = "//"

    override fun getBlockFilePath(block: Block): String {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
//        val namespace = block.namespace.replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + fileName
    }
}