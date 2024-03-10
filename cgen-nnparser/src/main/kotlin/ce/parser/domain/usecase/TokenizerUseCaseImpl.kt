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
            if (digit != null) {
                buffer.movePosBy(digit.name.length)
                result.add(digit)
                debugLine1.append(digit.name).append(" ")
                debugLine2.append(digit.id).append(", ")
                continue
            }
            val comment = checkString(buffer, dictionaries.map[Type.COMMENTS]!!) as Comment?
            if (comment != null) {
                buffer.movePosBy(comment.name.length)
                val end = if (comment.oneLineComment) "\n" else comment.multilineCommentEnd
                val commentString = buffer.readUntil(end, false, false)
                buffer.movePosBy(end.length)
                result.add(Word(
                    name = commentString,
                    type = Type.COMMENTS
                ))
                continue
            }
            val space = checkString(buffer, dictionaries.map[Type.SPACES]!!)
            if (space != null) {
                buffer.movePosBy(space.name.length)
                if (space.name == "\n") {
                    debugFindigs.append("> ")
                    debugFindigs.append(debugLine1)
                    debugFindigs.append("\n")
                    debugFindigs.append(debugLine2)
                    debugFindigs.append("\n")
                    debugLine1.clear()
                    debugLine2.clear()
                }
                continue
            }
            val operator = checkString(buffer, operators)
            if (operator != null) {
                buffer.movePosBy(operator.name.length)
                result.add(operator)
                debugLine1.append(operator.name).append(" ")
                debugLine2.append(operator.id).append(", ")
                continue
            }
            // name or keyword?
            val nextToken = nextToken(buffer, dictionaries)
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