package ce.parser.nnparser

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SourceBufferTest {
    @Test
    fun getLength() {
        val buffer = SourceBuffer(
            buffer = StringBuilder("0123456789"),
            startPos = 2,
            endPos = 4
        )
        assertEquals(2, buffer.length)
    }

    @Test
    fun substring() {
    }

    @Test
    fun subbuffer() {
    }
}