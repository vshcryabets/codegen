package ce.parser.nnparser

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
        pos += delta
    }

    fun substring(startPosition: Int, endPosition: Int): String = buffer.substring(startPosition, endPosition)

    fun subbuffer(endPosition: Int) = SourceBuffer(
        buffer = buffer,
        startPos = pos,
        endPos = endPosition
    )
}