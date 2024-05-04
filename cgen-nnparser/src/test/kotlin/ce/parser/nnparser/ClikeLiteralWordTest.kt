package ce.parser.nnparser

import ce.parser.domain.usecase.CheckStringInDictionaryUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ClikeLiteralWordTest {

    @Test
    fun testNotString() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        assertEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT,
            item.check(SourceBuffer("1234567")))
    }

    @Test
    fun testNotStringInPosition() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        assertEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT,
            item.check(SourceBuffer("a = \"AAA\"")))
    }

    @Test
    fun testNoCorrectEnd() {
        val item = ClikeLiteralWord(name = "CString", id = 1)
        assertEquals(
            CheckStringInDictionaryUseCase.EMPTY_RESULT,
            item.check(SourceBuffer("\"AAA"))
        )
    }

    @Test
    fun testStringInPosition() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        val result = item.check(SourceBuffer("\"AAA\"; //comment"))
        assertEquals(5, result.lengthInChars)
        assertEquals(1, result.results.size)
        assertEquals(1, result.results[0].id)
        assertEquals("\"AAA\"", result.results[0].name)
    }

    @Test
    fun testSplashQuoteString() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        val result = item.check(SourceBuffer("\"asd\\\"qwe\"; //comment"))
        assertEquals(10, result.lengthInChars)
        assertEquals(1, result.results.size)
        assertEquals(1, result.results[0].id)
        assertEquals("\"asd\\\"qwe\"", result.results[0].name)
    }
}