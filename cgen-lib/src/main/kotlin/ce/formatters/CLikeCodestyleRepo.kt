package ce.formatters

import ce.settings.CodeStyle

class CLikeCodestyleRepo(
    private val codeStyle: CodeStyle
): CodeStyleRepo {

    override val tab: String
    private val spaceBeforeClass: String
    override val preventEmptyBlocks: Boolean
    init {
        val builder = StringBuilder()
        for (a in 0..codeStyle.tabSize - 1) {
            builder.append(" ");
        }
        tab = builder.toString()
        builder.clear()
        for (a in 0..codeStyle.newLinesBeforeClass - 1) {
            builder.append(newLine());
        }
        spaceBeforeClass = builder.toString()
        preventEmptyBlocks = codeStyle.preventEmptyBlocks
    }
    override fun newLine(): String = "\n"
    override fun spaceBeforeClass(): String = spaceBeforeClass
    override fun addSpaceBeforeRegion(): Boolean = (codeStyle.newLinesBeforeClass > 0)
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentMid(): String = "*"
    override fun multilineCommentEnd(): String = "**/${newLine()}"
    override fun singleComment(): String = "// "
}