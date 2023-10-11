package ce.parser

import ce.defs.TargetExt
import ce.parser.domain.usecase.LoadDictionaryUseCaseImpl
import ce.parser.domain.usecase.StoreDictionaryUseCaseImpl
import ce.parser.domain.usecase.StoreWordDictionaryUseCaseImpl
import com.opencsv.CSVWriter
import generators.obj.input.Leaf
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths

fun storeTokens(fileName: String, tokens: List<Int>) {
    val dictionaryFile = File(fileName)
    dictionaryFile.outputStream().use {
        tokens.forEach { token ->
            it.write(token.toString().toByteArray())
            it.write(0x20)
        }
    }
}

fun readFileLineByLineUsingForEachLine(fileName: String): StringBuilder {
    val result = StringBuilder()
    File(fileName).forEachLine {
        result.append(it)
        result.append('\n')
    }
    return result
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

fun main(args: Array<String>) {
    if (args.size < 2) {
        error(
            """
            Please, specify: 
                - input file
                - input target [Cxx, kotlin, meta, etc]
            """
        )
    }
    val inputFileName = args[0]
    val target = TargetExt.findByName(args[1])

    val loadDictionaryUseCase = LoadDictionaryUseCaseImpl()
    val storeDictionaryUseCase = StoreDictionaryUseCaseImpl()
    val storeWordDictionaryUseCase = StoreWordDictionaryUseCaseImpl()

    val dictionaryName = "./${target.rawValue}_dictionary.csv"

    val path = Paths.get("").toAbsolutePath().toString()
    println("Working Directory = $path")

    println("Loading $dictionaryName")
    val dictionary = loadDictionaryUseCase(File(dictionaryName))

    println("Loading $inputFileName")
    val buffer = readFileLineByLineUsingForEachLine(inputFileName)
    println("Parsing $inputFileName")
    println("Buffer size ${buffer.length}")
    val result = buildLinear(buffer, 0, dictionary)

    println("Store updated dictionary")
    storeWordDictionaryUseCase(File(dictionaryName), result.wordsMap)
    storeDictionaryUseCase(File("./literals.csv"), result.literalsMap)
    storeDictionaryUseCase(File("./digits.csv"), result.digits)
    storeTokens("./tokens.txt", result.tokens)
}
