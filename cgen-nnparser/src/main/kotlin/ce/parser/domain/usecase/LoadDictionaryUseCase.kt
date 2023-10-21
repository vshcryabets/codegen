package ce.parser.domain.usecase

import ce.parser.Word
import ce.parser.WordDictionary
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader

interface LoadDictionaryUseCase {
    operator fun invoke(file: File): WordDictionary
}

class LoadDictionaryUseCaseImpl : LoadDictionaryUseCase {
    override fun invoke(file: File): WordDictionary {
        val wordsList = mutableListOf<Word>()
        if (file.exists()) {
            CSVReader(FileReader(file)).use { reader ->
                val allRecords = reader.readAll()
                allRecords.forEach {
                    val word = Word(
                        name = it[1],
                        nextIsLiteral = it[2].toBoolean(),
                        id = it[0].toInt()
                    )
                    wordsList.add(word)
                }
            }
        }
        return WordDictionary(
            wordsList = wordsList
        )
    }
}