package ce.parser.domain.usecase

import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import com.opencsv.CSVReader
import org.jetbrains.kotlin.javax.inject.Inject
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.InputStreamReader
import javax.script.Bindings
import javax.script.Compilable
import javax.script.ScriptEngine
import javax.script.ScriptException
import javax.script.SimpleBindings


interface LoadDictionaryUseCase {
    operator fun invoke(file: File, type: Type): WordDictionary
}

class LoadCsvDictionaryUseCaseImpl : LoadDictionaryUseCase {
    override fun invoke(file: File, type: Type): WordDictionary {
        val wordsList = mutableListOf<Word>()
        if (file.exists()) {
            CSVReader(FileReader(file)).use { reader ->
                val allRecords = reader.readAll()
                allRecords.forEach {
                    val word = Word(
                        name = it[1],
                        id = it[0].toInt(),
                        type = type
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

class LoadGroovyDictionaryUseCaseImpl @Inject constructor(
    private val groovyScriptEngine: ScriptEngine,
) : LoadDictionaryUseCase {
    override fun invoke(file: File, type: Type): WordDictionary {
        val wordsList = mutableListOf<Word>()
        if (file.exists()) {
            val reader = InputStreamReader(FileInputStream(file))
            val bindings: Bindings = SimpleBindings()
            bindings["words"] = wordsList
            try {
                val compEngine = groovyScriptEngine as Compilable
                val cs = compEngine.compile(reader)
                cs.eval(bindings)
//                groovyScriptEngine.eval(reader, bindings)
            } catch (error: ScriptException) {
                error("Error in file ${file.absoluteFile}:${error.lineNumber} column ${error.columnNumber} ${error.message} ")
                throw error
            }
        }
        return WordDictionary(
            wordsList = wordsList
        )
    }
}