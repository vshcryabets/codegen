package ce.entrypoints

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import generators.obj.input.Leaf
import java.io.File
import java.io.FileReader
import java.io.FileWriter


class Literal(name: String) : Leaf(name)
class Name(name: String) : Leaf(name)
class Word(name: String) : Leaf(name)
class Digit(name: String) : Leaf(name)

const val operators = "({}[].+-/*^%$#@!<>,"
const val spaces = " \n\r\t"
const val digits = "0123456789"
const val digitsHex = "0123456789ABCDEF"


fun loadDictionary(fileName: String, dictionary: MutableMap<Int, Word>) {
    val dictionaryFile = File(fileName)
    if (dictionaryFile.exists()) {
        CSVReader(FileReader(dictionaryFile)).use { reader ->
            val r = reader.readAll()
            r.forEach {
                dictionary[it[0].toInt()] = Word(it[1])
            }
        }
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
            it.writeNext(arrayOf(id.toString(), "\"${word.name}\""))
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
    var counter = 0
    var literalCounter = 1000000
    var digitCounter = 2000000

    val literalsMap = mutableMapOf<Int, Literal>()
    val digitisMap = mutableMapOf<Int, Digit>()

    val numbers = mutableListOf<Int>()

    val wordsMapRevers = mutableMapOf<String, Int>().apply {
        putAll(dictionary
            .onEach { if (it.key > counter) counter = it.key }
            .map { (key, value) ->
            value.name to key
        }.toMap())
    }

    var pos = inPos
    do {
        val ch = buffer.get(pos)
        if (ch == '"') {
            val literalPair = readLiteral(buffer, pos)
            literalsMap[literalCounter] = literalPair.first
            numbers.add(literalCounter)
            println("Literal \"${literalPair.first.name}\" = $literalCounter")
            literalCounter++
            pos = literalPair.second
        } else if (ch in spaces) {
            pos++
        } else if (ch in digits) {
            val digit = readDigit(buffer, pos)
            digitisMap[digitCounter] = digit.first
            numbers.add(digitCounter)
            digitCounter++
            pos = digit.second
        } else {
            val wordPair = readWord(buffer, pos)
            var id = counter
            if (!wordsMapRevers.containsKey(wordPair.first.name)) {
                dictionary[id] = wordPair.first
                wordsMapRevers[wordPair.first.name] = id
                counter++
            } else {
                id = wordsMapRevers[wordPair.first.name]!!
            }
            numbers.add(id)
            pos = wordPair.second
        }
    } while (pos < buffer.length)
    println(numbers.toString())
    return ParseResult(
        wordsMap = dictionary,
        digits = digitisMap,
        literalsMap = literalsMap,
        tokens = numbers,
    )
}

fun readWord(buffer: StringBuilder, startPos: Int): Pair<Word,Int> {
    var pos = startPos
    val ch = buffer.get(pos)
    if (ch in operators) {
        return Pair(Word(ch.toString()), pos+1)
    }
    val wordBuffer = StringBuilder()
    do {
        val ch = buffer.get(pos)
        if (ch in operators || ch in spaces) {
            break
        }
        wordBuffer.append(ch)
        pos++
    } while (pos < buffer.length)
    return Pair(Word(wordBuffer.toString()), pos)
}

fun readDigit(buffer: StringBuilder, startPos: Int): Pair<Digit,Int> {
    return Pair(Digit(""), 0)
}

fun readLiteral(buffer: StringBuilder, startPos: Int): Pair<Literal, Int> {
    var pos = startPos
    val left = buffer.length - pos
    if (left > 3) {
        if (buffer.get(pos) == '"' && buffer.get(pos + 1) == '"' && buffer.get(pos + 2) == '"') {
            // start """ literal
            TODO()
        }
    }
    if (buffer[pos] != '"') {
        throw IllegalStateException("Wrong literal start at $pos")
    }
    pos++
    var screenChar = false
    val literalBuffer = StringBuilder()
    do {
        val ch = buffer.get(pos)
        if (ch == '"' && !screenChar) {
            pos++
            break
        }
        screenChar = false
        literalBuffer.append(ch)
        if (ch == '\\') {
            screenChar = true
        }
        pos++
    } while (pos < buffer.length)
    return Pair(Literal(literalBuffer.toString()), pos)
}

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("""
            Please, specify: 
                - input file
            """)
    }

    val dictionary = mutableMapOf<Int, Word>()
    val dictionaryName = "./test/parser/kotlin_dictionary.csv"
    loadDictionary(dictionaryName, dictionary)

    val buffer = readFileLineByLineUsingForEachLine(args[0])
    val result = buildLinear(buffer, 0, dictionary)

    storeDictionary(dictionaryName, result.wordsMap)
    storeDictionary("./test/parser/literals.csv", result.literalsMap)
    storeDictionary("./test/parser/digits.csv", result.digits)
}
