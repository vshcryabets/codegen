package ce.parser.domain.usecase

import ce.parser.domain.TestDictionary
import ce.parser.nnparser.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CheckStringInDictionaryImplTest {

    fun getUseCase() = CheckStringInDictionaryImpl()
    @Test
    fun testSimpleWord() {
        val useCase = getUseCase()
        val result = useCase.invoke(
            buffer = SourceBuffer(StringBuilder("max"), 0),
            dictionary = TestDictionary.getDict(listOf("max", "float", "int"), 100, Type.KEYWORD)
        )
        assertNotNull(result)
    }

    @Test
    fun testIsEmpty() {
        val useCase = getUseCase()
        val result = useCase.invoke(
            buffer = SourceBuffer(StringBuilder("AAAA"), 0),
            dictionary = WordDictionary(
                listOf(
                    Word(name = "BBBB", id = 3000, type = Type.DIGIT)
                ),
                sortBySize = false
            )
        )
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun testRegexpWord() {
        val useCase = getUseCase()
        val result = useCase.invoke(
            buffer = SourceBuffer(StringBuilder("0xF9823"), 0),
            dictionary = WordDictionary(
                listOf(
                    RegexWord(name = "0x[\\dABCDEFabcdef]+", id = 3000, type = Type.DIGIT)
                ),
                sortBySize = false
            )
        )
        assertNotNull(result)
    }

    @Test
    fun testProgrammableWord() {
        val useCase = getUseCase()
        val result = useCase.invoke(
            buffer = SourceBuffer(StringBuilder("12345"), 0),
            dictionary = WordDictionary(
                listOf(
                    ProgrammableWordImpl(name = "P1", id = 3000, type = Type.DIGIT,
                        checkFnc = { buffer ->
                            CheckStringInDictionaryUseCase.EMPTY_RESULT
                        })
                ),
                sortBySize = false
            )
        )
        assertNotEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT, result)
    }
}