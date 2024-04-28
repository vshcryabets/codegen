package ce.parser.nnparser

import ce.parser.domain.usecase.CheckStringInDictionaryUseCase

enum class Type {
    SPACES,
    COMMENTS,
    OPERATOR,
    KEYWORD,
    NAME,
    DIGIT,
    UNKNOWN,
    STRING_LITERAL
}

interface WordItem {
    val name: String
    val type: Type
    val id: Int
}

interface ProgrammableWord : WordItem {
    fun check(buffer: SourceBuffer): CheckStringInDictionaryUseCase.Result
}

data class Word(
    override val name: String,
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,
) : WordItem

data class RegexWord(
    override val name: String,
    override val id: Int = -1,
    override val type: Type = Type.OPERATOR,

) : ProgrammableWord {
    private val regexObj: Regex = name.toRegex()

    override fun check(buffer:SourceBuffer):CheckStringInDictionaryUseCase.Result {
        val result = regexObj.matchAt(buffer.buffer, buffer.pos)
        return if (result == null) {
            CheckStringInDictionaryUseCase.EMPTY_RESULT
        } else if (result.range.start != buffer.pos) {
            CheckStringInDictionaryUseCase.EMPTY_RESULT
        } else {
            CheckStringInDictionaryUseCase.Result(
                results = listOf(
                    Word(
                        name = if (result.groups.size > 1) result.groups[1]!!.value else result.value,
                        id = id, type = type
                    )
                ),
                lengthInChars = result.value.length
            )
        }
    }
}

//data class ProgrammableWordImpl(
//    override val name: String,
//    override val id: Int = -1,
//    override val type: Type = Type.OPERATOR,
//    override val checkFnc: (SourceBuffer) -> CheckStringInDictionaryUseCase.Result
//) : ProgrammableWord

data class ClikeLiteralWord(
    override val name: String,
    override val id: Int = -1,
    override val type: Type = Type.STRING_LITERAL
) : ProgrammableWord {
    override fun check(buffer: SourceBuffer): CheckStringInDictionaryUseCase.Result =
        if (buffer.nextIs("\"")) {
            buffer.movePosBy(1)
            val findLiteral = buffer.readUntil { start, pos, end, buffer ->
                return@readUntil (buffer[pos] == '"' && pos > start && buffer[pos - 1] != '\\')
            }
            buffer.movePosBy(-1)
            if (findLiteral == null) {
                CheckStringInDictionaryUseCase.EMPTY_RESULT
            } else {
                CheckStringInDictionaryUseCase.Result(
                    results = listOf(
                        Word(
                            name = "\"$findLiteral\"",
                            id = id, type = type
                        )
                    ),
                    lengthInChars = findLiteral.length + 2
                )
            }
        } else {
            CheckStringInDictionaryUseCase.EMPTY_RESULT
        }

}