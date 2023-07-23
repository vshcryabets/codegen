package ce.formatters

interface CodeStyleRepo {
    val preventEmptyBlocks: Boolean
    fun multilineCommentStart() : String
    fun multilineCommentMid() : String
    fun multilineCommentEnd() : String
    fun singleComment() : String
    fun newLine() : String

    val tab: String

    fun spaceBeforeClass(): String
}