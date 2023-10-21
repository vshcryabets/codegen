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
                  position: Int,
                  spaces: WordDictionary,
                  operators: WordDictionary): String {
        return ""
    }

    override operator fun invoke(
        buffer: String,
        keywords: WordDictionary,
        operators: WordDictionary,
        spaces: WordDictionary,
    ): List<Word> {
        val result = mutableListOf<Word>()
        var pos = 0
        var markPos = 0
        while (pos < buffer.length) {
            val space = checkString(buffer, pos, spaces)
            if (space != null) {
                if (markPos != pos) {
                    val name = buffer.substring(markPos, pos)
                    result.add(Word(name = name, type = Type.NAME))
                }
                pos = pos + space.name.length
                markPos = pos
                continue
            }
            val operator = checkString(buffer, pos, operators)
            if (operator != null) {
                if (markPos != pos) {
                    val name = buffer.substring(markPos, pos)
                    result.add(Word(name = name, type = Type.NAME))
                }
                pos = pos + operator.name.length
                markPos = pos
                result.add(operator)
                continue
            }
            // TODO
            // 1 get next token
            // 2 check it with keywords list, if it present - is a keyword
            // 3 otherwise it a name
            val keyword = checkString(buffer, pos, keywords)
            if (keyword != null) {
//                val nextCharPos = pos + keyword.name.length + 1
//                if ((nextCharPos < buffer.length) && (buffer[nextCharPos].isLetterOrDigit())) {
//                    // this is not a keyword, it a name
//                    pos = pos + keyword.name.length
//                    continue
//                }
                if (markPos != pos) {
                    val name = buffer.substring(markPos, pos)
                    result.add(Word(name = name, type = Type.NAME))
                }
                pos = pos + keyword.name.length
                markPos = pos
                result.add(keyword)
                continue
            }
            pos++
        }
        return result
    }
}