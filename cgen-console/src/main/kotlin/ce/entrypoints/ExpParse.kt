package ce.entrypoints

import generators.obj.input.Leaf
import java.io.File

class Literal(name: String) : Leaf(name)
class Name(name: String) : Leaf(name)
class Word(name: String) : Leaf(name)

fun readFileLineByLineUsingForEachLine(fileName: String) : StringBuilder {
    val result = StringBuilder()
    File(fileName).forEachLine {
        result.append(it)
        result.append('\n')
    }
    return result
}

fun buildLinear(buffer: StringBuilder, inPos: Int) {
    val map = mutableMapOf<String, Int>()
    val result = mutableListOf<Int>()
    var pos = inPos
    do {
        val ch = buffer.get(pos)
        if (ch == '"') {
            val literal = readLiteral(buffer, pos)
        } else if (ch == ' ' || ch == '\n') {
            pos++
        } else {
            val word = readWord(buffer, pos)
        }
    } while (pos < buffer.length)

}

fun readWord(buffer: StringBuilder, pos: Int): Pair<Word,Int> {
    val ch = buffer.get(pos)
    if (ch in "({}[].+-/*^%$#@!<>,") {
        return Pair(Word(ch.toString()), pos+1)
    }
    do {
        val ch = buffer.get(pos)

    } while (pos < buffer.length)
    return Word("")
}

fun readLiteral(buffer: StringBuilder, pos: Int): Literal {
    return Literal("")
}

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("""
            Please, specify: 
                - input file
            """)
    }

    val buffer = readFileLineByLineUsingForEachLine(args[0])
    buildLinear(buffer, 0)
}
