package ce.parser.domain.usecase

import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

fun getDict(srings: List<String>, basiId: Int, type: Type): WordDictionary {
    var id = basiId
    return WordDictionary(
        srings.map {
            Word(
                name = it,
                type = type,
                id = id++
            )
        }
    )
}

class TokenizerUseCaseImplTest {

    val keywordDict = getDict(listOf("max", "float"), 100, Type.KEYWORD)
    val operatorsDict = getDict(listOf("(", ")", "=", "->", ",", "-", ">", "+"), 200, Type.OPERATOR)
    val spaces = getDict(listOf(" ", "\t", "\n"), 1000, Type.SPACES)

    @Test
    fun testSpaces() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <OP ->
        val result1 = tokenizer(
            buffer = "\t  \t- ",
            keywords = keywordDict,
            operators = operatorsDict,
            spaces = spaces
        )
        assertEquals(1, result1.size)
        assertEquals(Type.OPERATOR, result1[0].type)
        assertEquals("-", result1[0].name)

        // expected
        // <KEY max><OP +>
        val result2 = tokenizer(
            buffer = "  max +",
            keywords = keywordDict,
            operators = operatorsDict,
            spaces = spaces
        )
        assertEquals(2, result2.size)
        assertEquals(Type.KEYWORD, result2[0].type)
        assertEquals("max", result2[0].name)
        assertEquals(Type.OPERATOR, result2[1].type)
        assertEquals("+", result2[1].name)
    }

    @Test
    fun testName() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <Name x><=><max><(><)>
        val result = tokenizer(
            buffer = "x = max\n()",
            keywords = keywordDict,
            operators = operatorsDict,
            spaces = spaces)
        assertEquals(5, result.size)
        assertEquals(Type.NAME, result[0].type)
        assertEquals("x", result[0].name)
        assertEquals(Type.OPERATOR, result[1].type)
        assertEquals("=", result[1].name)
    }

    @Test
    fun testName2() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <KW float><Name max2><=><max><(><)>
        val result = tokenizer(
            buffer = "float max2=max()",
            keywords = keywordDict,
            operators = operatorsDict,
            spaces = spaces)
        assertEquals(6, result.size)
        assertEquals(Type.NAME, result[0].type)
        assertEquals("x", result[0].name)
        assertEquals(Type.OPERATOR, result[1].type)
        assertEquals("=", result[1].name)

        // tests
        // float max2=max(1,2)
        // qwe->varX
        // qwe-varX
        // qwe>varX
    }
}