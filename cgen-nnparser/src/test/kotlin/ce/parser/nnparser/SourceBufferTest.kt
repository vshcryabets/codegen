package ce.parser.nnparser

import org.junit.jupiter.api.Assertions.assertEquals
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
    fun testToString() {
        val buffer = SourceBuffer(
            text = "0123456789",
            startPos = 2,
            endPos = 9
        )
        buffer.movePosBy(3)
        val str = buffer.toString()
        assertEquals("2:5:9 >>234|5|678<<", str)
    }

    @Test
    fun testToStringAtEnd() {
        val buffer = SourceBuffer(text = "0123456789")
        buffer.movePosBy(10)
        val str = buffer.toString()
        assertEquals("0:10:10 >>0123456789||<<", str)
    }

    @Test
    fun substring() {
    }

    @Test
    fun subbuffer() {
    }
}