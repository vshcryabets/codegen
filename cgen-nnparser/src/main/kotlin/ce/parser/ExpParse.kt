package ce.parser

import ce.defs.Target
import ce.domain.usecase.execute.ExecuteScriptByExtUseCaseImpl
import ce.parser.domain.usecase.*
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.script.ScriptEngineManager
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory


fun storeTokens(fileName: String, tokens: List<Int>) {
    val dictionaryFile = File(fileName)
    dictionaryFile.outputStream().use {
        tokens.forEach { token ->
            it.write(token.toString().toByteArray())
            it.write(0x20)
        }
    }
}

data class ParseResult(
    val tokensMap: Map<Int, Word> = emptyMap(),
    val tokens: List<Int> = emptyList()
)

data class SampleData(
    val sampleName: String,
    val sourceFile: String,
    val sourceTarget: Target,
    val metaFile: String,
    val metaTarget: Target = Target.Meta,
    )

val globalSources = mutableListOf<SampleData>()
var globalOutputDirectory: String = "./expparse_out/"
var dictinariesDirectory: String = "./dictionary/"
var globalDicts = emptyMap<Target, TargetDictionaries>()

fun cleanSource() {
    globalSources.clear()
}

fun addSource(sampleName: String, sourceName: String, sourceTarget: Target, metaFile: String) {
    globalSources.add(SampleData(
        sampleName = sampleName,
        sourceFile = sourceName,
        sourceTarget = sourceTarget,
        metaFile = metaFile
    ))
}

fun main(args: Array<String>) {
    val factory = ScriptEngineManager()
    val groovyEngine = factory.getEngineByName("groovy")
    val ktsEngine = KotlinJsr223DefaultScriptEngineFactory().getScriptEngine()

    if (args.size < 1) {
        error(
            """
            Please, specify: 
                - input configuration file (.kts, .groovy)
            """
        )
    }
    val executeScriptUseCase = ExecuteScriptByExtUseCaseImpl(
        ktsScriptEngine = ktsEngine,
        groovyScriptEngine = groovyEngine
    )
    val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val calcScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val loadDictionaryUseCase = LoadDictionaryUseCaseImpl()
    val loadFileUseCase = LoadFileUseCaseImpl(ioScope)
    val loadTargetDictionaries = LoadTargetDictionariesUseCaseImpl(
        loadDictionaryUseCase = loadDictionaryUseCase,
        ioScope = ioScope
    )
    val loadAllDictionariesUseCase = LoadAllDictionariesUseCaseImpl(
        ioScope = ioScope,
        loadTargetDictionariesUseCase = loadTargetDictionaries
    )
    val buildLinearUseCase = BuildLinearUseCaseImpl2()
    val processSampleUseCase = ProcessSampleUseCaseImpl(calcScope, loadFileUseCase, buildLinearUseCase)
    val configFile = File(args[0])
    println("Execute $configFile")
    executeScriptUseCase(configFile)
    runBlocking {
        println("Load dictionaries from $dictinariesDirectory")
        globalDicts = loadAllDictionariesUseCase(dictinariesDirectory)
    }
    runBlocking {
        processSampleUseCase(globalSources.first(), globalOutputDirectory, globalDicts)
    }
    println("END")

    /*
    val loadDictionaryUseCase = LoadDictionaryUseCaseImpl()
    val storeDictionaryUseCase = StoreDictionaryUseCaseImpl()
    val storeWordDictionaryUseCase = StoreWordDictionaryUseCaseImpl()



    println("Parsing $inputFileName")
    println("Buffer size ${buffer.length}")
    val result = buildLinear(buffer, 0, dictionary)

    println("Store updated dictionary")
    storeWordDictionaryUseCase(File(dictionaryName), result.wordsMap)
    storeDictionaryUseCase(File("./literals.csv"), result.literalsMap)
    storeDictionaryUseCase(File("./digits.csv"), result.digits)
    storeTokens("./tokens.txt", result.tokens)*/
}
