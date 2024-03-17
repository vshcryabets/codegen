package ce.parser.domain.usecase

import ce.parser.domain.NamesDictionaryRepo
import ce.parser.nnparser.Comment
import ce.parser.nnparser.RegexWord
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordItem
import org.jetbrains.kotlin.javax.inject.Inject
import kotlin.text.StringBuilder

interface TokenizerUseCase {
    data class Result(
        val words: List<WordItem>,
        val debugFindings: StringBuilder,
        val namesDictionary: List<Word>
    )
    operator fun invoke(
        text: String,
        dictionaries: TargetDictionaries,
        nameBase: Int,
        digitBase: Int,
        debugFindings: Boolean = false,
    ): Result
}

class TokenizerUseCaseImpl @Inject constructor(
    private val checkString: CheckStringInDictionaryUseCase
) : TokenizerUseCase {

    fun nextToken(buffer: SourceBuffer,
                  dictinaries: TargetDictionaries,
                  ): String {
        val startPosition = buffer.pos
        val empty =  CheckStringInDictionaryUseCase.EMPTY_RESULT
        while (!buffer.end()) {
            if ((checkString(buffer, dictinaries.map[Type.SPACES]!!) != empty) ||
                (checkString(buffer, dictinaries.map[Type.COMMENTS]!!) != empty) ||
                (checkString(buffer, dictinaries.operators) != empty)) {
                break
            }
            buffer.movePosBy(1)
        }
        return buffer.substring(startPosition, buffer.pos)
    }

    override operator fun invoke(
        text: String,
        dictionaries: TargetDictionaries,
        nameBase: Int,
        digitBase: Int,
        debugFindings: Boolean
    ): TokenizerUseCase.Result {
        val namesDictionary = NamesDictionaryRepo(
            startId = nameBase
        )
        val debugFindigs = StringBuilder()
        val debugLine1= StringBuilder()
        val debugLine2 = StringBuilder()
        val buffer = SourceBuffer(StringBuilder(text), 0)
        val result = mutableListOf<WordItem>()
        val operators = dictionaries.operators
        while (!buffer.end()) {
            // check digit
            val digit = checkString(buffer, dictionaries.map[Type.DIGIT]!!)
            if (!digit.isEmpty()) {
                val debugLine = buffer.substring(buffer.pos, buffer.pos + digit.lengthInChars)
                buffer.movePosBy(digit.lengthInChars)
                result.addAll(digit.results)
                debugLine1.append(debugLine).append(" ")
                debugLine2.append(digit.results.map {
                    it.id
                }.joinToString(separator = ", ")).append(", ")
                continue
            }
            val comment = checkString(buffer, dictionaries.map[Type.COMMENTS]!!)
            if (!comment.isEmpty()) {
                val debugLine = buffer.substring(buffer.pos, buffer.pos + comment.lengthInChars)
                buffer.movePosBy(comment.lengthInChars)
                result.addAll(digit.results)
                debugLine1.append(debugLine).append(" ")
                debugLine2.append(comment.results.map {
                    it.id
                }.joinToString(separator = ", ")).append(", ")
                continue
            }
            val space = checkString(buffer, dictionaries.map[Type.SPACES]!!)
            if (!space.isEmpty()) {
                buffer.movePosBy(space.lengthInChars)
                space.results.forEach {
                    if (it.name == "\n") {
                        debugFindigs.append("> ")
                        debugFindigs.append(debugLine1)
                        debugFindigs.append("\n")
                        debugFindigs.append(debugLine2)
                        debugFindigs.append("\n")
                        debugLine1.clear()
                        debugLine2.clear()
                    }
                }
                continue
            }
            val operator = checkString(buffer, operators)
            if (!operator.isEmpty()) {
                val debugLine = buffer.substring(buffer.pos, buffer.pos + operator.lengthInChars)
                buffer.movePosBy(operator.lengthInChars)
                result.addAll(operator.results)
                debugLine1.append(debugLine).append(" ")
                debugLine2.append(operator.results.map {
                    it.id
                }.joinToString(separator = ", ")).append(", ")
                continue
            }
            // name or keyword?
            val nextToken = nextToken(buffer, dictionaries)
            // TOD
            val keyword = dictionaries.keywords.sortedByLengthDict.find {
                it.name.equals(nextToken)
            }
            if (keyword != null) {
                result.add(keyword)
                debugLine1.append(keyword.name).append(" ")
                debugLine2.append(keyword.id).append(", ")
                continue
            }
            // Word(name = nextToken, type = Type.NAME, id = 254)
            val nameToken = namesDictionary.search(nextToken)
            debugLine1.append(nameToken.name).append(" ")
            debugLine2.append(nameToken.id).append(", ")
            result.add(nameToken)
        }
        return TokenizerUseCase.Result(
            words = result,
            debugFindings = debugFindigs,
            namesDictionary = namesDictionary.exportToWordsList()
        )
    }
}