package ce.parser.domain.usecase

import ce.parser.Word
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader

interface LoadDictionaryUseCase {
    operator fun invoke(file: File) : MutableMap<Int, Word>
}

class LoadDictionaryUseCaseImpl : LoadDictionaryUseCase {
    override fun invoke(file: File): MutableMap<Int, Word> {
        val dictionary = mutableMapOf<Int, Word>()
        if (file.exists()) {
            CSVReader(FileReader(file)).use { reader ->
                val r = reader.readAll()
                r.forEach {
                    dictionary[it[0].toInt()] = Word(it[1], nextIsLiteral = it[2].toBoolean())
                }
            }
        }
        return dictionary
    }
}