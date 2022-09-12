package generators.obj

import ce.settings.CodeStyle

abstract class CLikeFileGenerator(codeStyle: CodeStyle) : FileGenerator(codeStyle) {

    override fun newLine(): String = "\n"
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentMid(): String = "*"
    override fun multilineCommentEnd(): String = "**/${newLine()}"
    override fun singleComment(): String = "//"
}