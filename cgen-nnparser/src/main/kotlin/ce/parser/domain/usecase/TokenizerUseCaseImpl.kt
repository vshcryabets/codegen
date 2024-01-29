package ce.parser.domain.usecase

import ce.parser.nnparser.Comment
import ce.parser.nnparser.RegexWord
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import ce.parser.nnparser.WordItem
import org.jetbrains.kotlin.javax.inject.Inject
import kotlin.text.StringBuilder

interface TokenizerUseCase {
    data class Result(
        val wordIds: List<WordItem>,
        val debugFindings: StringBuilder,
    )
    operator fun invoke(
        text: String,
        dictinaries: TargetDictionaries,
        debugFindings: Boolean = false
    ): Result
}

class TokenizerUseCaseImpl @Inject constructor() : TokenizerUseCase {
    fun checkString(buffer: SourceBuffer,
                    dictionary: WordDictionary
                    ): WordItem? {
        val iterator = dictionary.sortedByLengthDict.iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            if (it is RegexWord) {
                val word = buffer.nextIsRegexp(it.regexObj)
                if (word != null) {
                    return it.copy(name = word)
                }
            } else {
                if (buffer.nextIs(it.name, ignoreCase = false)) {
                    return it
                }
            }
        }
        return null
    }

    fun nextToken(buffer: SourceBuffer,
                  dictinaries: TargetDictionaries,
                  ): String {
        val startPosition = buffer.pos
        while (!buffer.end()) {
            if ((checkString(buffer, dictinaries.map[Type.SPACES]!!) != null) ||
                (checkString(buffer, dictinaries.map[Type.COMMENTS]!!) != null) ||
                (checkString(buffer, dictinaries.operators) != null)) {
                break
            }
            buffer.movePosBy(1)
        }
        return buffer.substring(startPosition, buffer.pos)
    }

    override operator fun invoke(
        text: String,
        dictinaries: TargetDictionaries,
        debugFindings: Boolean
    ): TokenizerUseCase.Result {
        val debugFindigs = StringBuilder()
        val buffer = SourceBuffer(StringBuilder(text), 0)
        val result = mutableListOf<WordItem>()
        while (!buffer.end()) {
            // check digit
            val digit = checkString(buffer, dictinaries.map[Type.DIGIT]!!) as RegexWord?
            if (digit != null) {
                buffer.movePosBy(digit.name.length)
                result.add(digit)
                continue
            }
            val comment = checkString(buffer, dictinaries.map[Type.COMMENTS]!!) as Comment?
            if (comment != null) {
                buffer.movePosBy(comment.name.length)
                val end = if (comment.oneLineComment) "\n" else comment.multilineCommentEnd
                val commentString = buffer.readUntil2(end, false, false)
                buffer.movePosBy(end.length)
                result.add(Word(
                    name = commentString,
                    type = Type.COMMENTS
                ))
                continue
            }
            val space = checkString(buffer, dictinaries.map[Type.SPACES]!!)
            if (space != null) {
                buffer.movePosBy(space.name.length)
                continue
            }
            val operator = checkString(buffer, dictinaries.operators)
            if (operator != null) {
                buffer.movePosBy(operator.name.length)
                result.add(operator)
                continue
            }
            // name or keyword?
            val nextToken = nextToken(buffer, dictinaries)
            val keyword = dictinaries.keywords.sortedByLengthDict.find {
                it.name.equals(nextToken)
            }
            if (keyword != null) {
                result.add(keyword)
                continue
            }
            result.add(Word(name = nextToken, type = Type.NAME))
        }
        return TokenizerUseCase.Result(
            wordIds = result,
            debugFindings = debugFindigs
        )
    }
}