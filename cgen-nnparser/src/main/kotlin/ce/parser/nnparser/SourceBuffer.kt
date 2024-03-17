package ce.parser.nnparser

import kotlin.math.min

class SourceBuffer(
    val buffer: StringBuilder,
    val startPos: Int = 0,
    val endPos: Int = buffer.length
) {

    val length: Int
        get() = endPos - startPos

    var pos: Int = startPos
        private set(value) {
            field = value
        }

    constructor(
        text: String,
        startPos: Int = 0,
        endPos: Int = text.length,
    ) : this(
        buffer = StringBuilder(text),
        startPos = startPos,
        endPos = endPos
    )

    fun end(): Boolean = pos >= endPos
    fun nextIs(s: String, ignoreCase: Boolean = false): Boolean {
        if (pos + s.length > endPos) {
            // out of buffer size
            return false
        }
        val substr = buffer.subSequence(pos, pos + s.length).toString()
        return substr.equals(s, ignoreCase)
    }

    fun readUntil(end: String,
                  ignoreCase: Boolean,
                  includeEnd: Boolean): String {
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
        } while (pos < endPos)
        return wordBuffer.toString()
    }

    fun movePosBy(delta: Int) {
        pos = min(endPos, pos + delta)
    }

    fun substring(startPosition: Int, endPosition: Int): String = buffer.substring(startPosition, endPosition)

    fun subbuffer(endPosition: Int) = SourceBuffer(
        buffer = buffer,
        startPos = pos,
        endPos = endPosition
    )

    override fun toString(): String {
        val lpreview = substring(maxOf(pos - 10, startPos), minOf(pos, endPos))
        val cpreview = substring(minOf(maxOf(pos, startPos), endPos), minOf(pos + 1, endPos))
        val rpreview = substring(minOf(maxOf(pos + 1, startPos), endPos), minOf(pos + 10, endPos))
        return "$startPos:$pos:$endPos >>$lpreview|$cpreview|$rpreview<<"
    }
}