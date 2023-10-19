package ce.parser.domain.usecase

import ce.parser.Word
import ce.parser.WordDictionary
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader

interface LoadDictionaryUseCase {
    operator fun invoke(file: File) : WordDictionary
}

class LoadDictionaryUseCaseImpl : LoadDictionaryUseCase {
    override fun invoke(file: File): WordDictionary {
        val dictionary = mutableMapOf<Int, Word>()
        if (file.exists()) {
            CSVReader(FileReader(file)).use { reader ->
                val r = reader.readAll()
                r.forEach {
                    dictionary[it[0].toInt()] = Word(it[1], nextIsLiteral = it[2].toBoolean())
                }
            }
        }
        var wordsCounter = 0
        val reverseMap = mutableMapOf<String, Int>().apply {
            putAll(dictionary
                .onEach { if (it.key > wordsCounter) wordsCounter = it.key }
                .map { (key, value) ->
                    value.name to key
                }.toMap()
            )
        }
        wordsCounter++
        return WordDictionary(
            dictionary = dictionary,
            reverse = reverseMap,
            maxId = wordsCounter
        )
    }
}