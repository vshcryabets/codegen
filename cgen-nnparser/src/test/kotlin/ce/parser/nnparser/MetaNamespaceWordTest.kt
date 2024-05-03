package ce.parser.nnparser

import ce.parser.domain.usecase.CheckStringInDictionaryUseCase
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MetaNamespaceWordTest {

    @Test
    fun textCheck() {
        val obj = MetaNamespaceWord()
        val sourceBuffer = SourceBuffer("namespace(\"a.b.c\")")
        val result = obj.check(sourceBuffer)
        assertNotEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT, result)
    }
}