package ce.parser.domain.usecase

import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import org.jetbrains.kotlin.javax.inject.Inject

interface TokenizerUseCase {
    operator fun invoke(
        buffer: String,
        dictinaries: TargetDictionaries,
    ): List<Word>
}

class TokenizerUseCaseImpl @Inject constructor() : TokenizerUseCase {
    fun checkString(buffer: String,
                    position: Int,
                    dictionary: WordDictionary
                    ): Word? {
        val substr = buffer.substring(position)
        val possibleWord = dictionary.sortedByLengthDict.find {
            substr.startsWith(it.name)
        }
        return possibleWord
    }

    fun nextToken(buffer: String,
                  startPosition: Int,
                  dictinaries: TargetDictionaries,
                  ): String {
        var pos = startPosition
        while (pos < buffer.length) {
            if ((checkString(buffer, pos, dictinaries.spaces) != null) ||
                (checkString(buffer, pos, dictinaries.comments) != null) ||
                (checkString(buffer, pos, dictinaries.operators) != null)) {
                break
            }
            pos++
        }
        return buffer.substring(startPosition, pos)
    }

    override operator fun invoke(
        buffer: String,
        dictinaries: TargetDictionaries,
    ): List<Word> {
        val result = mutableListOf<Word>()
        var pos = 0
        while (pos < buffer.length) {
            // TODO at fist place check for comment
            val space = checkString(buffer, pos, dictinaries.spaces)
            if (space != null) {
                pos += space.name.length
                continue
            }
            val operator = checkString(buffer, pos, dictinaries.operators)
            if (operator != null) {
                pos += operator.name.length
                result.add(operator)
                continue
            }
            val nextToken = nextToken(buffer, pos, dictinaries)
            val keyword = dictinaries.keywords.sortedByLengthDict.find {
                it.name.equals(nextToken)
            }
            if (keyword != null) {
                pos += keyword.name.length
                result.add(keyword)
                continue
            }
            result.add(Word(name = nextToken, type = Type.NAME))
            pos += nextToken.length
        }
        return result
    }
}