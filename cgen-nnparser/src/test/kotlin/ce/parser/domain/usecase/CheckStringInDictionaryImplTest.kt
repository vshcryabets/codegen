package ce.parser.domain.usecase

import ce.parser.domain.TestDictionary
import ce.parser.domain.dictionaries.StaticDictionary
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
            dictionary = StaticDictionary(
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
            dictionary = StaticDictionary(
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
        val emptyResult = useCase.invoke(
            buffer = SourceBuffer("12345"),
            dictionary = StaticDictionary(
                listOf(
                    object: ProgrammableWord {
                        override val name = "NOT"
                        override val id = 3000
                        override val type = Type.DIGIT
                        override fun check(buffer: SourceBuffer) = CheckStringInDictionaryUseCase.EMPTY_RESULT
                    }
                ),
                sortBySize = false
            )
        )
        val oneResult = useCase.invoke(
            buffer = SourceBuffer("12345"),
            dictionary = StaticDictionary(
                listOf(
                    object: ProgrammableWord {
                        override val name = "NOT"
                        override val id = 3000
                        override val type = Type.DIGIT
                        override fun check(buffer: SourceBuffer) = CheckStringInDictionaryUseCase.EMPTY_RESULT
                    },
                    object: ProgrammableWord {
                        override val name = "TRUE"
                        override val id = 3000
                        override val type = Type.DIGIT
                        override fun check(buffer: SourceBuffer) =
                            CheckStringInDictionaryUseCase.Result(
                                results = listOf(Word(name = "name")),
                                lengthInChars = buffer.length
                            )
                    }
                ),
                sortBySize = false
            )
        )
        assertEquals(CheckStringInDictionaryUseCase.EMPTY_RESULT, emptyResult)
        assertEquals(1, oneResult.results.size)
    }
}