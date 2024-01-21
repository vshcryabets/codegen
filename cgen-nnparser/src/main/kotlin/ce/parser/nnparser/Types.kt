package ce.parser.nnparser

enum class Type {
    SPACES,
    COMMENTS,
    OPERATOR,
    KEYWORD,
    NAME,
    DIGIT,
    LITERAL,
    UNKNOWN
}

interface WordItem {
    val name: String
    val type: Type
    val id: Int
}

data class Comment(
    override val name: String,
    val oneLineComment: Boolean = true,
    val multilineCommentEnd: String = "",
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR
) : WordItem

data class Word(
    override val name: String,
    val nextIsLiteral: Boolean = false,
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,
) : WordItem

data class RegexWord(
    override val name: String,
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,
    val regexObj: Regex = Regex(name)
) : WordItem