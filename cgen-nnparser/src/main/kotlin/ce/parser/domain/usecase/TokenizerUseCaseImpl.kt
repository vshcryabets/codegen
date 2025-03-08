package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.natural.DynamicDictionaries
import ce.parser.domain.dictionaries.natural.StaticDictionaries
import ce.parser.domain.dictionaries.natural.StaticDictionary
import ce.parser.nnparser.*
import org.jetbrains.kotlin.javax.inject.Inject

interface TokenizerUseCase {
    data class Result(
        val words: List<WordItem>,
        val debugFindings: StringBuilder,
        val namesDictionary: List<WordItem>,
        val digitsDictionary: List<WordItem>,
        val stringsDictionary: List<WordItem>,
    )
    operator fun invoke(
        buffer: SourceBuffer,
        dictionaries: StaticDictionaries,
        dynamicDictionaries: DynamicDictionaries,
        debugFindings: Boolean = false,
    ): Result
}

open class TokenizerUseCaseImpl @Inject constructor(
    private val checkString: CheckStringInDictionaryUseCase
) : TokenizerUseCase {

    fun nextToken(buffer: SourceBuffer,
                  staticDictionaries: StaticDictionaries,
                  ): String {
        val startPosition = buffer.pos
        val empty =  CheckStringInDictionaryUseCase.EMPTY_RESULT
        while (!buffer.end()) {
            if ((checkString(buffer, staticDictionaries.getSpaces()) != empty) ||
                (checkString(buffer, staticDictionaries.getComments()) != empty) ||
                (checkString(buffer, staticDictionaries.getOperators()) != empty)) {
                break
            }
            buffer.movePosBy(1)
        }
        return buffer.substring(startPosition, buffer.pos)
    }

    fun findInDictionary(
        buffer: SourceBuffer,
        dictionary: StaticDictionary,
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
        staticDictionaries: StaticDictionaries,
        dynamicDictionaries: DynamicDictionaries,
        debugFindings: Boolean
    ): TokenizerUseCase.Result {
        val debugFindigs = StringBuilder()
        val debugLine1= StringBuilder()
        val debugLine2 = StringBuilder()
        val result = mutableListOf<WordItem>()
        while (!buffer.end()) {
            if (checkSpace(buffer,
                staticDictionaries,
                debugLine1,
                debugLine2,
                debugFindigs
                )) {
                continue
            }
            if (lookInStaticDictionaries(buffer, result,
                    debugLine1, debugLine2, staticDictionaries)) {
                continue
            }

            // name or keyword?
            val nextToken = nextToken(buffer, staticDictionaries)
            val keyword = staticDictionaries.getKeywords().search(nextToken)
            if (keyword != null) {
                result.add(keyword)
                debugLine1.append(keyword.name).append(" ")
                debugLine2.append(keyword.id).append(", ")
                continue
            }
            // Word(name = nextToken, type = Type.NAME, id = 254)
            val nameToken = dynamicDictionaries.getNamesDictionary().search(nextToken)
            debugLine1.append(nameToken.name).append(" ")
            debugLine2.append(nameToken.id).append(", ")
            result.add(nameToken)
        }
        // put digit id's
        val resultWithDigits = result.map {
            when (it.type) {
                Type.DIGIT -> dynamicDictionaries.getDigitsDictionary().search(it.name)
                Type.STRING_LITERAL -> dynamicDictionaries.getStringDictionary().search(it.name)
                else -> it
            }
        }
        return TokenizerUseCase.Result(
            words = resultWithDigits,
            debugFindings = debugFindigs,
            namesDictionary = dynamicDictionaries.getNamesDictionary().exportToWordsList(),
            digitsDictionary = dynamicDictionaries.getDigitsDictionary().exportToWordsList(),
            stringsDictionary = dynamicDictionaries.getStringDictionary().exportToWordsList()
        )
    }

    open protected fun checkSpace(buffer: SourceBuffer,
                                  staticDictionaries: StaticDictionaries,
                                  debugLine1: StringBuilder,
                                  debugLine2: StringBuilder,
                                  debugFindings: StringBuilder): Boolean {
        val space = checkString(buffer, staticDictionaries.getSpaces())
        if (!space.isEmpty()) {
            buffer.movePosBy(space.lengthInChars)
            space.results.forEach {
                if (it.name == "\n") {
                    debugFindings.append("> ")
                    debugFindings.append(debugLine1)
                    debugFindings.append("\n")
                    debugFindings.append(debugLine2)
                    debugFindings.append("\n")
                    debugLine1.clear()
                    debugLine2.clear()
                }
            }
            return true
        }
        return false
    }

    open protected fun lookInStaticDictionaries(
        buffer: SourceBuffer,
        result: MutableList<WordItem>,
        debugLine1: StringBuilder,
        debugLine2: StringBuilder,
        staticDictionaries: StaticDictionaries
    ): Boolean {
        if (findInDictionary(buffer, staticDictionaries.getStringLiterals(),
                result, debugLine1, debugLine2)) {
            return true
        }
        // check digit
        if (findInDictionary(buffer, staticDictionaries.getDigits(),
                result, debugLine1, debugLine2)) {
            return true
        }
        if (findInDictionary(buffer, staticDictionaries.getComments(),
                result, debugLine1, debugLine2)) {
            return true
        }
        if (findInDictionary(buffer, staticDictionaries.getOperators(),
                result, debugLine1, debugLine2)) {
            return true
        }
        return false
    }
}