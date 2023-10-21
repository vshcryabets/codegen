package ce.parser.nnparser

enum class Type {
    OPERATOR,
    KEYWORD,
    NAME,
    DIGIT,
    LITERAL
}

data class Word(
    val name: String,
    val nextIsLiteral: Boolean = false,
    val id: Int = -1,
    val type: Type = Type.OPERATOR
)