package ce.parser.domain.usecase

import ce.parser.nnparser.ProgrammableWord
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.WordDictionary
import ce.parser.nnparser.WordItem

interface CheckStringInDictionaryUseCase {
    data class Result(
        val results: List<WordItem>,
        val lengthInChars: Int,
    ) {
        fun isEmpty() = results.isEmpty()
    }

    companion object {
        val EMPTY_RESULT = Result(
            results = emptyList(),
            lengthInChars = 0
        )
    }
    operator fun invoke(buffer: SourceBuffer,
                        dictionary: WordDictionary
    ): Result
}

class CheckStringInDictionaryImpl: CheckStringInDictionaryUseCase {
    override operator fun invoke(buffer: SourceBuffer,
                                 dictionary: WordDictionary
    ): CheckStringInDictionaryUseCase.Result {
        val iterator = dictionary.sortedByLengthDict.iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            if (it is ProgrammableWord) {
                val checkResult = it.checkFnc(buffer)
                if (!checkResult.isEmpty()) {
                    return checkResult
                }
            } else {
                if (buffer.nextIs(it.name, ignoreCase = false)) {
                    return CheckStringInDictionaryUseCase.Result(
                        results = listOf(it),
                        lengthInChars = it.name.length
                    )
                }
            }
        }
        return CheckStringInDictionaryUseCase.EMPTY_RESULT
    }
}