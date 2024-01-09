package ce.parser.domain.usecase

import ce.parser.nnparser.Comment
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import ce.parser.nnparser.WordItem
import org.jetbrains.kotlin.javax.inject.Inject
import java.lang.StringBuilder

interface TokenizerUseCase {
    operator fun invoke(
        text: String,
        dictinaries: TargetDictionaries,
    ): List<WordItem>
}

class TokenizerUseCaseImpl @Inject constructor() : TokenizerUseCase {
    fun checkString(buffer: SourceBuffer,
                    dictionary: WordDictionary
                    ): WordItem? {
        val possibleWord = dictionary.sortedByLengthDict.find {
            buffer.nextIs(it.name, ignoreCase = false)
        }
        return possibleWord
    }

    fun nextToken(buffer: SourceBuffer,
                  dictinaries: TargetDictionaries,
                  ): String {
        val startPosition = buffer.pos
        while (!buffer.end()) {
            if ((checkString(buffer, dictinaries.spaces) != null) ||
                (checkString(buffer, dictinaries.comments) != null) ||
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
    ): List<WordItem> {
        val buffer = SourceBuffer(StringBuilder(text), 0)
        val result = mutableListOf<WordItem>()
        while (!buffer.end()) {
            if (buffer.nextIn("0123456789")) {
                // we found a digit
                val digitString = buffer.readWhile { it in "0123456789." }
                result.add(Word(
                    name = digitString,
                    type = Type.DIGIT
                ))
                continue

            }
            val comment = checkString(buffer, dictinaries.comments) as Comment?
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
            val space = checkString(buffer, dictinaries.spaces)
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
        return result
    }
}