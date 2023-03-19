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

fun readFileLineByLineUsingForEachLine(fileName: String) : StringBuilder {
    val result = StringBuilder()
    File(fileName).forEachLine {
        result.append(it)
        result.append('\n')
    }
    return result
}

data class ParseResult(
    val wordsMap : Map<Int, Word>
)

fun buildLinear(buffer: StringBuilder, inPos: Int, dictionary: MutableMap<Int, Word>) : ParseResult {
    var counter = 0
    val wordsMap = mutableMapOf<Int, Word>().apply {
        putAll(dictionary)
    }
    val wordsMapRevers = mutableMapOf<String, Int>().apply {
        putAll(dictionary
            .onEach { if (it.key > counter) counter = it.key }
            .map { (key, value) ->
            value.name to key
        }.toMap())
    }

    val literalsMap = mutableMapOf<Int, Literal>()
    val digitisMap = mutableMapOf<Int, Digit>()
    val numbers = mutableListOf<Int>()
    var pos = inPos
    do {
        val ch = buffer.get(pos)
        if (ch == '"') {
            val literalPair = readLiteral(buffer, pos)
            literalsMap[counter] = literalPair.first
            numbers.add(counter)
            counter++
            println("Literal \"${literalPair.first.name}")
            pos = literalPair.second
        } else if (ch in spaces) {
            pos++
        } else if (ch in digits) {
            val digit = readDigit(buffer, pos)
        } else {
            val wordPair = readWord(buffer, pos)
            var id = counter
            if (!wordsMapRevers.containsKey(wordPair.first.name)) {
                wordsMap[id] = wordPair.first
                wordsMapRevers[wordPair.first.name] = id
                counter++
            } else {
                id = wordsMapRevers[wordPair.first.name]!!
            }
            numbers.add(id)

            println(wordPair.first.name)
            pos = wordPair.second
        }
    } while (pos < buffer.length)
    println(numbers.toString())
    return ParseResult(
        wordsMap = wordsMap
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

    val dictionaryFile = File("file.csv")
    if (dictionaryFile.exists()) {
        CSVReader(FileReader(dictionaryFile)).use { reader ->
            val r = reader.readAll()
            r.forEach {
                dictionary[it[0].toInt()] = Word(it[1])
            }
        }
    }

    val buffer = readFileLineByLineUsingForEachLine(args[0])
    val result = buildLinear(buffer, 0, dictionary)

    CSVWriter(FileWriter(dictionaryFile),
        CSVWriter.DEFAULT_SEPARATOR,
        CSVWriter.NO_QUOTE_CHARACTER,
        CSVWriter.NO_ESCAPE_CHARACTER,
        CSVWriter.DEFAULT_LINE_END).use {
        result.wordsMap.forEach { id, word ->
            it.writeNext(arrayOf(id.toString(), "\"${word.name}\""))
        }
    }
}
