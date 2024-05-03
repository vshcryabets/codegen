package ce.parser.domain.usecase

import ce.parser.domain.NamesDictionaryRepo
import ce.parser.nnparser.*
import org.jetbrains.kotlin.javax.inject.Inject

interface TokenizerUseCase {
    data class Result(
        val words: List<WordItem>,
        val debugFindings: StringBuilder,
        val namesDictionary: List<Word>,
        val digitsDictionary: List<Word>,
        val stringsDictionary: List<Word>,
    )
    operator fun invoke(
        buffer: SourceBuffer,
        dictionaries: TargetDictionaries,
        namesDictionary: NamesDictionaryRepo,
        digitsDictionary: NamesDictionaryRepo,
        stringLiteralsDictionary: NamesDictionaryRepo,
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

    fun findInDictionary(
        buffer: SourceBuffer,
        dictionary: WordDictionary,
        resultsList: MutableList<WordItem>,
        debugLine1: StringBuilder,
        debugLine2: StringBuilder,
    ): Boolean {
        val searchResult = checkString(buffer, dictionary)
        if (!searchResult.isEmpty()) {
            val debugLine = buffer.substring(buffer.pos, buffer.pos + searchResult.lengthInChars)
            buffer.movePosBy(searchResult.lengthInChars)
            resultsList.addAll(searchResult.results)
            debugLine1.append(debugLine).append(" ")
            debugLine2.append(searchResult.results.map {
                it.id
            }.joinToString(separator = ", ")).append(", ")
        }
        return !searchResult.isEmpty()
    }

    override operator fun invoke(
        buffer: SourceBuffer,
        dictionaries: TargetDictionaries,
        namesDictionary: NamesDictionaryRepo,
        digitsDictionary: NamesDictionaryRepo,
        stringLiteralsDictionary: NamesDictionaryRepo,
        debugFindings: Boolean
    ): TokenizerUseCase.Result {
        val debugFindigs = StringBuilder()
        val debugLine1= StringBuilder()
        val debugLine2 = StringBuilder()
        val result = mutableListOf<WordItem>()
        while (!buffer.end()) {
            // check string literal
            if (findInDictionary(buffer, dictionaries.stringLiterals, result, debugLine1, debugLine2)) {
                continue
            }
            // check digit
            if (findInDictionary(buffer, dictionaries.digits, result, debugLine1, debugLine2)) {
                continue
            }

            if (findInDictionary(buffer, dictionaries.comments, result, debugLine1, debugLine2)) {
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
            if (findInDictionary(buffer, dictionaries.operators, result, debugLine1, debugLine2)) {
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
        // put digit id's
        val resultWithDigits = result.map {
            when (it.type) {
                Type.DIGIT -> digitsDictionary.search(it.name)
                Type.STRING_LITERAL -> stringLiteralsDictionary.search(it.name)
                else -> it
            }
        }
        return TokenizerUseCase.Result(
            words = resultWithDigits,
            debugFindings = debugFindigs,
            namesDictionary = namesDictionary.exportToWordsList(),
            digitsDictionary = digitsDictionary.exportToWordsList(),
            stringsDictionary = stringLiteralsDictionary.exportToWordsList()
        )
    }
}