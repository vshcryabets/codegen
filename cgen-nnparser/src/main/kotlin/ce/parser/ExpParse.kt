package ce.parser

import ce.defs.Target
import ce.domain.usecase.execute.ExecuteScriptByExtUseCaseImpl
import ce.parser.domain.usecase.*
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
    val wordsMap: Map<Int, Word> = emptyMap(),
    val literalsMap: Map<Int, Literal> = emptyMap(),
    val digits: Map<Int, Digit> = emptyMap(),
    val tokens: List<Int> = emptyList()
)

fun buildLinear(buffer: StringBuilder, inPos: Int, dictionary: MutableMap<Int, Word>): ParseResult {
    println("buildLinear")
    val srcBuffer = SourceBuffer(buffer, inPos)
    var wordsCounter = 0
    var literalCounter = 1000000
    var digitCounter = 2000000

    val literalsMap = mutableMapOf<Int, Literal>()
    val digitisMap = mutableMapOf<Int, Digit>()

    val numbers = mutableListOf<Int>()

    var prevWord = Word("")

    val wordsMapRevers = mutableMapOf<String, Int>().apply {
        putAll(dictionary
            .onEach { if (it.key > wordsCounter) wordsCounter = it.key }
            .map { (key, value) ->
                value.name to key
            }.toMap()
        )
    }
    wordsCounter++

//    var pos = inPos
    do {

        val ch = srcBuffer.getNextChar()
//        println("Process ${srcBuffer.pos} ${buffer.length} $ch")
        // TODO support for // and /* */
        if (srcBuffer.nextIs("//")) {
            val literalPair = srcBuffer.readUntil("\n", false, true)
            literalsMap[literalCounter] = Literal(literalPair.first)
            numbers.add(literalCounter)
            println("Comment \"${literalPair.first}\" = $literalCounter")
            literalCounter++
        } else if (srcBuffer.nextIs("/*")) {
            val strPair = srcBuffer.readUntil("*/", false, true)
            literalsMap[literalCounter] = Literal(strPair.first)
            numbers.add(literalCounter)
            println("Comment multiline \"${strPair.first}\" = $literalCounter")
            literalCounter++
        } else if (srcBuffer.nextIs("\"")) {
            val literalPair = srcBuffer.readLiteral()
            literalsMap[literalCounter] = literalPair.first
            numbers.add(literalCounter)
            println("Literal \"${literalPair.first.name}\" = $literalCounter")
            literalCounter++
        } else if (srcBuffer.nextIn(SourceBuffer.spaces)) {
            srcBuffer.skipChar()
        } else if (srcBuffer.nextIn(SourceBuffer.digits)) {
            val digit = srcBuffer.readDigit()
            digitisMap[digitCounter] = digit.first
            numbers.add(digitCounter)
            digitCounter++
        } else {
            val wordPair = srcBuffer.readWord()


            if (!wordsMapRevers.containsKey(wordPair.first.name)) {

                if (prevWord.nextIsLiteral) {
                    literalsMap[literalCounter] = Literal(wordPair.first.name)
                    numbers.add(literalCounter)
                    println("Literal \"${wordPair.first.name}\" = $literalCounter")
                    literalCounter++
                    prevWord = Word("")
                } else {
                    // add to dictionary
                    dictionary[wordsCounter] = wordPair.first
                    wordsMapRevers[wordPair.first.name] = wordsCounter
                    numbers.add(wordsCounter)
                    wordsCounter++
                    prevWord = wordPair.first
                }
            } else {
                var id = wordsMapRevers[wordPair.first.name]!!
                numbers.add(id)
                prevWord = dictionary[id]!!
            }
        }
    } while (!srcBuffer.end())
    println(numbers.toString())
    return ParseResult(
        wordsMap = dictionary,
        digits = digitisMap,
        literalsMap = literalsMap,
        tokens = numbers,
    )
}

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
    val processSampleUseCase = ProcessSampleUseCaseImpl(calcScope, loadFileUseCase)
    val configFile = File(args[0])
    println("Execute $configFile")
    executeScriptUseCase(configFile)
    runBlocking {
        println("Load dictionaries from $dictinariesDirectory")
        globalDicts = loadAllDictionariesUseCase(dictinariesDirectory)
    }
    runBlocking {
        processSampleUseCase(globalSources.first(), globalOutputDirectory)
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
