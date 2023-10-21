package ce.parser.domain.usecase

import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

fun getDict(srings: List<String>, basiId: Int): WordDictionary {
    var id = basiId
    return WordDictionary(
        srings.map {
            Word(
                name = it,
                id = id++
            )
        }
    )
}

class TokenizerUseCaseImplTest {

    val keywordDict = getDict(listOf("max", "float"), 100)
    val operatorsDict = getDict(listOf("(", ")", "=", "->", ",", "-", ">"), 200)

    @Test
    fun testTokenizer() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <Name x><=><max><(><)>
        val result = tokenizer.invoke(
            buffer = "x=max()",
            keywords = keywordDict,
            operators = operatorsDict)
        assertEquals(5, result.size)
        assertEquals(Type.NAME, result[0].type)
        assertEquals("x", result[0].name)
        assertEquals(Type.OPERATOR, result[1].type)
        assertEquals("=", result[1].name)

        // tests
        // float max2=max(1,2)
        // qwe->varX
    }
}