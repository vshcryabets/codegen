package ce.parser.domain.usecase

import ce.parser.nnparser.*

interface CheckStringInDictionaryUseCase {
    operator fun invoke(buffer: SourceBuffer,
                        dictionary: WordDictionary
    ): WordItem?
}

class CheckStringInDictionaryImpl: CheckStringInDictionaryUseCase {
    override operator fun invoke(buffer: SourceBuffer,
                                 dictionary: WordDictionary
    ): WordItem? {
        val iterator = dictionary.sortedByLengthDict.iterator()
        while (iterator.hasNext()) {
            val it = iterator.next()
            if (it is ProgrammableWord) {
                val word = it.checkFnc(buffer)
                if (word != null) {
                    return Word(
                        name = word.value,
                        type = it.type,
                        id = it.id
                    )
                }
            } else {
                if (buffer.nextIs(it.name, ignoreCase = false)) {
                    return it
                }
            }
        }
        return null
    }
}