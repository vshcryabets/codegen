package ce.parser

import ce.defs.Target
import ce.domain.usecase.execute.ExecuteScriptByExtUseCaseImpl
import ce.parser.domain.dictionaries.NamesDictionary
import ce.parser.domain.usecase.*
import ce.parser.domain.dictionaries.DynamicDictionariesImpl
import ce.parser.domain.dictionaries.StaticDictionaries
import ce.parser.nnparser.Type
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
var globalDicts = emptyMap<Target, StaticDictionaries>()
const val defaultCapacity = 1000000
var globalNameBase = 1000000
var globalNameMax = globalNameBase + defaultCapacity
var globalDigitBase = globalNameMax
var globalDigitMax = globalDigitBase + defaultCapacity
var globalStringLiteralsBase = globalDigitMax
var globalStringLiteralsMax = globalStringLiteralsBase + defaultCapacity

fun cleanSource() {
    globalSources.clear()
}

fun addSource(sampleName: String,
              sourceName: String,
              sourceTarget: Target,
              metaFile: String) {
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
    val loadDictionaryUseCase = LoadCsvDictionaryUseCaseImpl()
    val loadGroovyDictUseCase = LoadGroovyDictionaryUseCaseImpl(
        groovyScriptEngine = groovyEngine
    )
    val loadFileUseCase = LoadFileUseCaseImpl(ioScope)
    val writeResultsUseCase = WriteResultsUseCaseImpl(
        ioScope = ioScope
    )
    val loadTargetDictionaries = LoadTargetDictionariesUseCaseImpl(
        loadCsvDictionaryUseCase = loadDictionaryUseCase,
        loadGroovyDictionaryUseCase = loadGroovyDictUseCase,
        ioScope = ioScope
    )
    val loadAllDictionariesUseCase = LoadAllDictionariesUseCaseImpl(
        ioScope = ioScope,
        loadTargetDictionariesUseCase = loadTargetDictionaries
    )
    val checkStringInDictionary = CheckStringInDictionaryImpl();
    val tokenizer = TokenizerUseCaseImpl(
        checkString = checkStringInDictionary
    )
    val metaTokenizer = MetaTokenizerUseCaseImpl(
        checkStringInDictionaryUseCase = checkStringInDictionary
    )
    val processSampleUseCase = ProcessSampleUseCaseImpl(
        calcScope = calcScope,
        loadFileUseCase =  loadFileUseCase,
        tokenizerUseCase = tokenizer,
        metaTokenizerUseCase = metaTokenizer,
        writeResultsUseCase = writeResultsUseCase)
    val configFile = File(args[0])
    println("Execute $configFile")
    executeScriptUseCase(configFile)
    runBlocking {
        println("Load dictionaries from $dictinariesDirectory")
        globalDicts = loadAllDictionariesUseCase(dictinariesDirectory)
    }
    val namesDictionary = NamesDictionary(
        startId = globalNameBase,
        maxId = globalNameMax,
        type = Type.NAME
    )
    val digitsDictionary = NamesDictionary(
        startId = globalDigitBase,
        maxId = globalDigitMax,
        type = Type.DIGIT
    )
    val stringLiteralsDictionary = NamesDictionary(
        startId = globalStringLiteralsBase,
        maxId = globalStringLiteralsMax,
        type = Type.DIGIT
    )
    val dynamiDictionaries = DynamicDictionariesImpl(namesDictionary, digitsDictionary, stringLiteralsDictionary)
    runBlocking {
        processSampleUseCase(
            globalSources.first(), globalOutputDirectory, globalDicts,
            dynamicDictionaries = dynamiDictionaries,
        )
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
