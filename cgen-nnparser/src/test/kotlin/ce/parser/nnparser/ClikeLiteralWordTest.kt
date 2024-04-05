package ce.parser.nnparser

import ce.parser.domain.usecase.CheckStringInDictionaryUseCase
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ClikeLiteralWordTest {

    @Test
    fun testNotString() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        assertEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT,
            item.checkFnc(SourceBuffer("1234567")))
    }

    @Test
    fun testNotStringInPosition() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        assertEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT,
            item.checkFnc(SourceBuffer("a = \"AAA\"")))
    }

    @Test
    fun testStringInPosition() {
        val item = ClikeLiteralWord(name = "CString", id =1)
        val result = item.checkFnc(SourceBuffer("\"AAA\"; //comment"))
        assertEquals(5, result.lengthInChars)
        assertEquals(1, result.results.size)
        assertEquals(1, result.results[0].id)
        assertEquals("\"AAA\"", result.results[0].id)
    }
}