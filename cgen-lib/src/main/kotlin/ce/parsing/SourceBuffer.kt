package ce.parsing

import generators.obj.input.Leaf
import generators.obj.input.Node

class Literal(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?) = Literal(name, parent)
}
class Name(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?) = Name(name, parent)
}
class Word(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?) = Word(name, parent)
}
class Digit(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?) = Digit(name, parent)
}

class SourceBuffer(
    private val buffer: StringBuilder,
    startPos: Int
) {
    companion object {
        const val operators = "({}[].+-/*^%$#@!<>,;"
        const val spaces = " \n\r\t "
        const val digits = "0123456789"
        const val digitsHex = "0123456789ABCDEF"
    }

    var pos: Int = startPos

    fun getNextChar(): Char = buffer.get(pos)

    fun readLiteral(): Pair<Literal, Int> {
        var logicPos = pos
        val left = buffer.length - logicPos
        if (left > 3) {
            if (buffer.get(logicPos) == '"' && buffer.get(logicPos + 1) == '"' && buffer.get(logicPos + 2) == '"') {
                // start """ literal
                TODO()
            }
        }
        if (buffer[logicPos] != '"') {
            throw IllegalStateException("Wrong literal start at $logicPos")
        }
        logicPos++
        var screenChar = false
        val literalBuffer = StringBuilder()
        do {
            val ch = buffer.get(logicPos)
            if (ch == '"' && !screenChar) {
                logicPos++
                break
            }
            screenChar = false
            literalBuffer.append(ch)
            if (ch == '\\') {
                screenChar = true
            }
            logicPos++
        } while (logicPos < buffer.length)
        pos = logicPos
        return Pair(Literal(literalBuffer.toString()), logicPos)
    }

    fun skipChar() {
        pos++
    }

    fun readDigit(): Pair<Digit, Int> {
        val readBuffer = StringBuilder()
        while (getNextChar() in digits) {
            readBuffer.append(getNextChar())
            pos++
        }
        return Pair(Digit(readBuffer.toString()), pos)
    }

    fun readWord(): Pair<Word, Int> {
        var ch = buffer.get(pos)
        if (ch in operators) {
            pos++
            return Pair(Word(ch.toString()), pos)
        }
        val wordBuffer = StringBuilder()
        do {
            ch = buffer.get(pos)
            if (ch in operators || ch in spaces) {
                break
            }
            wordBuffer.append(ch)
            pos++
        } while (pos < buffer.length)
        return Pair(Word(wordBuffer.toString()), pos)
    }

    fun end(): Boolean = pos >= buffer.length
    fun nextIs(s: String, ignoreCase: Boolean = false): Boolean {
        if (pos + s.length > buffer.length) {
            // out of buffer size
            return false
        }
        val substr = buffer.subSequence(pos, pos + s.length).toString()
        return substr.equals(s, ignoreCase)
    }

    fun nextIn(variants: String): Boolean = buffer.get(pos) in variants

    fun readUntil(end: String,
                  ignoreCase: Boolean,
                  includeEnd: Boolean): Pair<String, Int> {
        val wordBuffer = StringBuilder()
        do {
            if (nextIs(end, ignoreCase)) {
                if (includeEnd) {
                    wordBuffer.append(end)
                    pos += end.length
                }
                break
            }
            val ch = buffer.get(pos)
            pos++
            wordBuffer.append(ch)
        } while (pos < buffer.length)
        return Pair(wordBuffer.toString(), pos)
    }

}