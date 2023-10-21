package ce.parser.domain.usecase

import ce.parser.nnparser.Word
import com.opencsv.CSVWriter
import generators.obj.input.Leaf
import java.io.File
import java.io.FileWriter

interface StoreDictionaryUseCase {
    operator fun invoke(file: File, dictionary: Map<Int, Leaf>)
}

interface StoreWordDictionaryUseCase {
    operator fun invoke(file: File, dictionary: Map<Int, Word>)
}

class StoreDictionaryUseCaseImpl : StoreDictionaryUseCase {
    override fun invoke(file: File, dictionary: Map<Int, Leaf>) {
        CSVWriter(
            FileWriter(file),
            CSVWriter.DEFAULT_SEPARATOR,
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.NO_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        ).use {
            dictionary.forEach { id, leaf ->
                it.writeNext(arrayOf(id.toString(), "\"${leaf.name.replace("\n", "\\n")}\""))
            }
        }
    }
}

class StoreWordDictionaryUseCaseImpl : StoreWordDictionaryUseCase {
    override fun invoke(file: File, dictionary: Map<Int, Word>) {
        CSVWriter(
            FileWriter(file),
            CSVWriter.DEFAULT_SEPARATOR,
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.NO_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        ).use {
            dictionary.forEach { id, word ->
                it.writeNext(arrayOf(id.toString(), "\"${word.name.replace("\n", "\\n")}\"", "\"${word.nextIsLiteral}\""))
            }
        }
    }
}