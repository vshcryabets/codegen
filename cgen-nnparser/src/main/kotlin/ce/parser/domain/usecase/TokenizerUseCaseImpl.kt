package ce.parser.domain.usecase

import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import org.jetbrains.kotlin.javax.inject.Inject

interface TokenizerUseCase {
    operator fun invoke(
        buffer: String,
        keywords: WordDictionary,
        dictionary: WordDictionary,
        spaces: WordDictionary,
        comments: WordDictionary,
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
                  spaces: WordDictionary,
                  comments: WordDictionary,
                  operators: WordDictionary): String {
        var pos = startPosition
        while (pos < buffer.length) {
            if ((checkString(buffer, pos, spaces) != null) ||
                (checkString(buffer, pos, comments) != null) ||
                (checkString(buffer, pos, operators) != null)) {
                break
            }
            pos++
        }
        return buffer.substring(startPosition, pos)
    }

    override operator fun invoke(
        buffer: String,
        keywords: WordDictionary,
        operators: WordDictionary,
        spaces: WordDictionary,
        comments: WordDictionary,
    ): List<Word> {
        val result = mutableListOf<Word>()
        var pos = 0
        var markPos = 0
        while (pos < buffer.length) {
            // TODO at fist place check for comment
            val space = checkString(buffer, pos, spaces)
            if (space != null) {
//                if (markPos != pos) {
//                    val name = buffer.substring(markPos, pos)
//                    result.add(Word(name = name, type = Type.NAME))
//                }
                pos = pos + space.name.length
//                markPos = pos
                continue
            }
            val operator = checkString(buffer, pos, operators)
            if (operator != null) {
//                if (markPos != pos) {
//                    val name = buffer.substring(markPos, pos)
//                    result.add(Word(name = name, type = Type.NAME))
//                }
                pos = pos + operator.name.length
//                markPos = pos
                result.add(operator)
                continue
            }
            // TODO
            // 1 get next token
            // 2 check it with keywords list, if it present - is a keyword
            // 3 otherwise it a name
            val nextToken = nextToken(buffer, pos, spaces, comments, operators)
            val keyword = keywords.sortedByLengthDict.find {
                it.name.equals(nextToken)
            }
            if (keyword != null) {
//                if (markPos != pos) {
//                    val name = buffer.substring(markPos, pos)
//                    result.add(Word(name = name, type = Type.NAME))
//                }
                pos = pos + keyword.name.length
//                markPos = pos
                result.add(keyword)
                continue
            }
            result.add(Word(name = nextToken, type = Type.NAME))
            pos = pos + nextToken.length
        }
        return result
    }
}