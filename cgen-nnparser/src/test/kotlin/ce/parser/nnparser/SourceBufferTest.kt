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
        val buffer = SourceBuffer(text = "54A134216547")
        assertEquals("54A1342", buffer.substring(0, 7))
    }

    @Test
    fun subbuffer() {
    }

    @Test
    fun readUntilTest() {
        val buffer = SourceBuffer(text = "54A134216547")
        val res = buffer.readUntil { start, pos, end, buffer ->
            (buffer[pos] == '1' && pos > start && buffer[pos - 1] != 'A')
        }
        assertEquals("54A1342", res)
    }

    @Test
    fun readUntilTest2() {
        val buffer = SourceBuffer(text = "A1A1")
        val res = buffer.readUntil { start, pos, end, buffer ->
            (buffer[pos] == '1' && pos > start && buffer[pos - 1] != 'A')
        }
        assertEquals(null, res)
    }

    @Test
    fun readUntilTest3() {
        val buffer = SourceBuffer(text = "A1A11")
        val res = buffer.readUntil { start, pos, end, buffer ->
            (buffer[pos] == '1' && pos > start && buffer[pos - 1] != 'A')
        }
        assertEquals("A1A1", res)
    }
}