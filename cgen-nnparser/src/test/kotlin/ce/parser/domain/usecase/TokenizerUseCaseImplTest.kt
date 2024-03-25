package ce.parser.domain.usecase

import ce.parser.domain.TestDictionary
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class TokenizerUseCaseImplTest {

    val DIGIT_BASE = 3000
    val NAME_BASE = 4000

    fun prepareTokenizer(): TokenizerUseCaseImpl {
        val checkStringInDictionary = CheckStringInDictionaryImpl()
        return TokenizerUseCaseImpl(
            checkString = checkStringInDictionary
        )
    }

    @Test
    fun testSpaces() {
        val tokenizer = prepareTokenizer()
        // expected
        // <OP ->
        val result = tokenizer(
            buffer = SourceBuffer("\t  \t- "),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(1, wordIds.size)
        assertEquals(Type.OPERATOR, wordIds[0].type)
        assertEquals("-", wordIds[0].name)
        assertEquals(205, wordIds[0].id)

        // expected
        // <KEY max><OP +>
        val result2 = tokenizer(
            buffer = SourceBuffer("  max +"),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds2 = result2.words
        assertEquals(2, wordIds2.size)
        assertEquals(Type.KEYWORD, wordIds2[0].type)
        assertEquals("max", wordIds2[0].name)
        assertEquals(100, wordIds2[0].id)
        assertEquals(Type.OPERATOR, wordIds2[1].type)
        assertEquals("+", wordIds2[1].name)
        assertEquals(207, wordIds2[1].id)
    }

    @Test
    fun testName() {
        val tokenizer = prepareTokenizer()
        // expected
        // <Name x><=><max><(><)>
        val result = tokenizer(
            buffer = SourceBuffer("x = max\n()"),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val words = result.words
        assertEquals(5, words.size)
        assertEquals(Type.NAME, words[0].type)
        assertEquals("x", words[0].name)
        assertEquals(NAME_BASE + 0, words[0].id)
        assertEquals(Type.OPERATOR, words[1].type)
        assertEquals(202, words[1].id)
        // check names words list
        assertEquals(1, result.namesDictionary.size)
        val name1 = result.namesDictionary[0]
        assertEquals(Type.NAME, name1.type)
        assertEquals("x", name1.name)
        assertEquals(NAME_BASE + 0, name1.id)
    }

    // TODO add name ids check below
    @Test
    fun testName2() {
        val tokenizer = prepareTokenizer()
        // expected
        // <KW float><Name max2><=><max><(><)>
        val result = tokenizer(
            buffer = SourceBuffer("float max2=max()"),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals("float", wordIds[0].name)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals("max2", wordIds[1].name)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals("=", wordIds[2].name)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals("max", wordIds[3].name)
    }

    @Test
    fun testCommentLine() {
        val tokenizer = prepareTokenizer()
        // expected
        // <Comment comment><Name a><=><max><(><)>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                //1+comment
                a=max()
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size)
        assertEquals(Type.COMMENTS, wordIds[0].type)
        assertEquals("1+comment", wordIds[0].name)
    }

    @Test
    fun testCommentLine2() {
        val tokenizer = prepareTokenizer()
        // expected
        // <Name a><=><max><(><)><Comment comment>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                a=max() //1+comment
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size)
        assertEquals(Type.NAME, wordIds[0].type)
        assertEquals("a", wordIds[0].name)
    }

    @Test
    fun testVariableDeclaration() {
        val tokenizer = prepareTokenizer()
        // expected
        // <float><max2><=><max><(><1><,><2><)>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                float max2=max(1,2)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(9, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals(Type.DIGIT, wordIds[5].type)
        assertEquals(Type.DIGIT, wordIds[7].type)
    }

    @Test
    fun testDigitWithPoint() {
        val tokenizer = prepareTokenizer()
        // expected
        // <float><max2><=><max><(><1><,><2><)>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                float max2=max(1.2,2.341)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(9, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals(Type.OPERATOR, wordIds[4].type)
        assertEquals(Type.DIGIT, wordIds[5].type)
        assertEquals(Type.OPERATOR, wordIds[6].type)
        assertEquals(Type.DIGIT, wordIds[7].type)
        assertEquals(Type.OPERATOR, wordIds[8].type)
    }

    @Test
    fun testDigitWithPointNegative() {
        val tokenizer = prepareTokenizer()
        // expected
        // <float><max2><=><max><(><1><,><-><2><)>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                float max2=max(1.2,-2.341)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(10, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals(Type.OPERATOR, wordIds[4].type)
        assertEquals(Type.DIGIT, wordIds[5].type)
        assertEquals(Type.OPERATOR, wordIds[6].type)
        assertEquals(Type.OPERATOR, wordIds[7].type)
        assertEquals(Type.DIGIT, wordIds[8].type)
        assertEquals(Type.OPERATOR, wordIds[9].type)
    }

    @Test
    fun testDigitHex() {
        val tokenizer = prepareTokenizer()
        // expected
        // <int><a><=><0x1234>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                int a=0xFAB
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(4, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.DIGIT, wordIds[3].type)
    }

    @Test
    fun testMultiline() {
        val tokenizer = prepareTokenizer()
        // expected
        // <#><pragma><once><namespace><name qwe>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                #pragma once

                namespace com
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(5, wordIds.size)
        assertEquals(Type.OPERATOR, wordIds[0].type)
        assertEquals(Type.KEYWORD, wordIds[1].type)
        assertEquals(Type.KEYWORD, wordIds[2].type)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals(Type.NAME, wordIds[4].type)
    }

    @Test
    fun testParseDoubleColon() {
        val tokenizer = prepareTokenizer()
        // expected
        // <namespace><name qwe><operator ::><name goldman><operator ::><name dt1>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                namespace com::goldman::dt1
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size, "Wrong word ids number")
    }

    @Test
    fun testParseStringAssignement() {
        val tokenizer = prepareTokenizer()
        // expected
        // <name a><operator =><literal "Simple string"><operator ;>
        val result = tokenizer(
            buffer = SourceBuffer(
                """
                a = "Simple string";
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            nameBase = NAME_BASE,
            digitBase = DIGIT_BASE,
        )
        val wordIds = result.words
        assertEquals(4, wordIds.size, "Wrong word ids number")
    }

    // tests
    // /*
    // comment multiline
    // */
    // check ids
    // check debugFindings
    // float max2=max(1,-2)
    // float max2=max(.5,.25)
    // qwe->varX
    // qwe-varX
    // qwe>varX
    // b=max();a = "Simple string max()";
}