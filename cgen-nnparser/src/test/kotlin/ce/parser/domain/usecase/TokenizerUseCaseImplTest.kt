package ce.parser.domain.usecase

import ce.parser.Name
import ce.parser.Word
import ce.parser.WordDictionary
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TokenizerUseCaseImplTest {
    val dict = WordDictionary(
        listOf("max", "float", "(", ")", "=", "->",",")
            .map {
                Word(it)
            }
    )

    @Test
    fun testTokenizer() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <Name x><=><max><(><)>
        val result = tokenizer.invoke("x=max()", dict)
        assertEquals(5, result.size)
        assertEquals(Name::class, result[0]::class)
        assertEquals("x", result[0].name)
        assertEquals(Word::class, result[1]::class)
        assertEquals("=", result[1].name)

        // tests
        // float max2=max(1,2)
        // qwe->varX
    }
}