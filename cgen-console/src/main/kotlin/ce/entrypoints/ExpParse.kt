package ce.entrypoints

import ce.defs.TargetExt
import ce.parsing.Digit
import ce.parsing.Literal
import ce.parsing.SourceBuffer
import ce.parsing.Word
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import generators.obj.input.Leaf
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Paths

fun loadDictionary(fileName: String, dictionary: MutableMap<Int, Word>) {
    val dictionaryFile = File(fileName)
    if (dictionaryFile.exists()) {
        CSVReader(FileReader(dictionaryFile)).use { reader ->
            val r = reader.readAll()
            r.forEach {
                dictionary[it[0].toInt()] = Word(it[1], nextIsLiteral = it[2].toBoolean())
            }
        }
    } else {
        throw FileNotFoundException("Dictionary $fileName not found")
    }
}

fun storeDictionary(fileName: String, dictionary: Map<Int, Leaf>) {
    val dictionaryFile = File(fileName)
    CSVWriter(FileWriter(dictionaryFile),
        CSVWriter.DEFAULT_SEPARATOR,
        CSVWriter.NO_QUOTE_CHARACTER,
        CSVWriter.NO_ESCAPE_CHARACTER,
        CSVWriter.DEFAULT_LINE_END).use {
        dictionary.forEach { id, word ->
            it.writeNext(arrayOf(id.toString(), "\"${word.name.replace("\n", "\\n")}\""))
        }
    }
}

fun storeTokens(fileName: String, tokens: List<Int>) {
    val dictionaryFile = File(fileName)
    dictionaryFile.outputStream().use {
        tokens.forEach {token ->
            it.write(token.toString().toByteArray())
            it.write(0x20)
        }
    }
}

fun readFileLineByLineUsingForEachLine(fileName: String) : StringBuilder {
    val result = StringBuilder()
    File(fileName).forEachLine {
        result.append(it)
        result.append('\n')
    }
    return result
}

data class ParseResult(
    val wordsMap : Map<Int, Word> = emptyMap(),
    val literalsMap : Map<Int, Literal> = emptyMap(),
    val digits : Map<Int, Digit> = emptyMap(),
    val tokens: List<Int> = emptyList()
)

fun buildLinear(buffer: StringBuilder, inPos: Int, dictionary: MutableMap<Int, Word>) : ParseResult {
    println("buildLinear")
    val srcBuffer = SourceBuffer(buffer, inPos)
    var counter = 0
    var literalCounter = 1000000
    var digitCounter = 2000000

    val literalsMap = mutableMapOf<Int, Literal>()
    val digitisMap = mutableMapOf<Int, Digit>()

    val numbers = mutableListOf<Int>()

    var prevWord = Word("")

    val wordsMapRevers = mutableMapOf<String, Int>().apply {
        putAll(dictionary
            .onEach { if (it.key > counter) counter = it.key }
            .map { (key, value) ->
            value.name to key
        }.toMap())
    }
    counter++

//    var pos = inPos
    do {

        val ch = srcBuffer.getNextChar()
        println("Process ${srcBuffer.pos} ${buffer.length} $ch")
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
            if (prevWord.nextIsLiteral) {
                literalsMap[literalCounter] = Literal(wordPair.first.name)
                numbers.add(literalCounter)
                println("Literal \"${wordPair.first.name}\" = $literalCounter")
                literalCounter++
                prevWord = Word("")
            } else {
                var id = counter
                if (!wordsMapRevers.containsKey(wordPair.first.name)) {
                    dictionary[id] = wordPair.first
                    wordsMapRevers[wordPair.first.name] = id
                    counter++
                } else {
                    id = wordsMapRevers[wordPair.first.name]!!
                    prevWord = dictionary[id]!!
                }
                numbers.add(id)
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
        error("""
            Please, specify: 
                - input file
                - input target [Cxx, kotlin, etc]
            """)
    }
    val inputFileName = args[0]
    val target = TargetExt.findByName(args[1])

    val dictionary = mutableMapOf<Int, Word>()
    val dictionaryName = "./${target.rawValue}_dictionary.csv"

    val path = Paths.get("").toAbsolutePath().toString()
    println("Working Directory = $path")

    println("Loading $dictionaryName")
    try {
        loadDictionary(dictionaryName, dictionary)
    } catch (error: Exception) {
        println("Error! $error")
    }

    println("Loading $inputFileName")
    val buffer = readFileLineByLineUsingForEachLine(inputFileName)
    println("Parsing $inputFileName")
    println("Buffer size ${buffer.length}")
    val result = buildLinear(buffer, 0, dictionary)

    println("Store updated dictionary")
    storeDictionary(dictionaryName, result.wordsMap)
    storeDictionary("./literals.csv", result.literalsMap)
    storeDictionary("./digits.csv", result.digits)
    storeTokens("./tokens.txt", result.tokens)
}
