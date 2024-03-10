package ce.parser.nnparser

class SourceBuffer(
    val buffer: StringBuilder,
    startPos: Int
) {
    companion object {
        const val operators = "({}[].+-/*^%$#@!<>,;"
        const val spaces = " \n\r\t "
        const val digits = "0123456789"
        const val digitsHex = "0123456789ABCDEF"
    }

    val length: Int
        get() = buffer.length
    var pos: Int = startPos
        private set(value) {
            field = value
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

//    fun nextIsRegexp(regexp: Regex): String? {
//        val result = regexp.find(buffer, pos)
//        if (result == null) {
//            return null
//        } else if (result.range.start != pos) {
//            return null
//        }
//        return result.value
//    }

    fun nextIsFnc(checkFnc: (StringBuilder, Int)-> MatchResult?): String? {
        val result = checkFnc(buffer, pos)
        if (result == null) {
            return null
        } else if (result.range.start != pos) {
            return null
        }
        return result.value
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
        } while (pos < buffer.length)
        return wordBuffer.toString()
    }

    fun movePosBy(delta: Int) {
        pos += delta
    }

    fun substring(startPosition: Int, endPosition: Int): String = buffer.substring(startPosition, endPosition)

}