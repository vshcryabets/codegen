package ce.parser.nnparser

import ce.parser.domain.TestDictionary
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegexWordTest {
    @Test
    fun testOneLineComment() {
        val wordCheck = TestDictionary.comments.sortedByLengthDict[0]
        val result = (wordCheck as RegexWord).checkFnc(SourceBuffer("//123456"))
        assertFalse(result.isEmpty())
        assertEquals(8,  result.lengthInChars)
        assertEquals(1, result.results.size)
        assertEquals("123456", result.results[0].name)
        assertEquals(2000, result.results[0].id)
        assertEquals(Type.COMMENTS, result.results[0].type)
    }
}