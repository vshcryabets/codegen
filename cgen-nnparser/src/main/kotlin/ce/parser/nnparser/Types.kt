package ce.parser.nnparser

enum class Type {
    SPACES,
    COMMENTS,
    OPERATOR,
    KEYWORD,
    NAME,
    DIGIT,
    LITERAL,
    UNKNOWN,
    STRING_LITERAL
}

interface WordItem {
    val name: String
    val type: Type
    val id: Int
}

interface ProgrammableWord: WordItem {
    val checkFnc: (SourceBuffer)-> MatchResult?
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
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,
) : WordItem

data class RegexWord(
    override val name: String,
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,
    val regexObj: Regex = Regex(name),
    override val checkFnc: (SourceBuffer) -> MatchResult? = { buffer ->
        val result = regexObj.find(buffer.buffer, buffer.pos)
        if (result == null) {
            null
        } else if (result.range.start != buffer.pos) {
            null
        } else {
            result
        }
    }
) : ProgrammableWord

data class ProgrammableWordImpl(
    override val name: String,
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,
    override val checkFnc: (SourceBuffer)-> MatchResult?
) : ProgrammableWord