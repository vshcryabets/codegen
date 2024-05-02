package ce.parser.domain.usecase

import ce.parser.domain.NamesDictionaryRepo
import ce.parser.domain.TestDictionary
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

data class PreparedEnv(
    val tokenizer: TokenizerUseCaseImpl,
    val namesDictionaryRepo: NamesDictionaryRepo,
    val digitsDictionaryRepo: NamesDictionaryRepo,
    val stringLiteralsDictionaryRepo: NamesDictionaryRepo,
)

class TokenizerUseCaseImplTest {

    val DIGIT_BASE = 13000
    val NAME_BASE = 14000
    val STRINGS_BASE = 15000

    fun prepareTokenizer(): PreparedEnv {
        val checkStringInDictionary = CheckStringInDictionaryImpl()
        return PreparedEnv(
            tokenizer = TokenizerUseCaseImpl(
                checkString = checkStringInDictionary
            ),
            digitsDictionaryRepo = NamesDictionaryRepo(
                startId = DIGIT_BASE,
                maxId = NAME_BASE,
                type = Type.DIGIT
            ),
            namesDictionaryRepo = NamesDictionaryRepo(
                startId = NAME_BASE,
                maxId = STRINGS_BASE,
                type = Type.NAME
            ),
            stringLiteralsDictionaryRepo = NamesDictionaryRepo(
                startId = STRINGS_BASE,
                maxId = STRINGS_BASE + 1000,
                type = Type.STRING_LITERAL
            )
        )
    }

    @Test
    fun testSpaces() {
        val env = prepareTokenizer()
        // expected
        // <OP ->
        val result = env.tokenizer(
            buffer = SourceBuffer("\t  \t- "),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(1, wordIds.size)
        assertEquals(Type.OPERATOR, wordIds[0].type)
        assertEquals("-", wordIds[0].name)
        assertEquals(205, wordIds[0].id)

        // expected
        // <KEY max><OP +>
        val result2 = env.tokenizer(
            buffer = SourceBuffer("  max +"),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
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
        val env = prepareTokenizer()
        // expected
        // <Name x><=><max><(><)>
        val result = env.tokenizer(
            buffer = SourceBuffer("x = max\n()"),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
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
        val env = prepareTokenizer()
        // expected
        // <KW float><Name max2><=><max><(><)>
        val result = env.tokenizer(
            buffer = SourceBuffer("float max2=max()"),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
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
        assertEquals(1, result.namesDictionary.size)
    }

    @Test
    fun testCommentLine() {
        val env = prepareTokenizer()
        // expected
        // <Comment comment><Name a><=><max><(><)>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                //1+comment
                a=max()
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size)
        assertEquals(Type.COMMENTS, wordIds[0].type)
        assertEquals("1+comment", wordIds[0].name)
        assertEquals(1, result.namesDictionary.size)
    }

    @Test
    fun testCommentLine2() {
        val env = prepareTokenizer()
        // expected
        // <Name a><=><max><(><)><Comment comment>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                a=max() //1+comment
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size)
        assertEquals(Type.NAME, wordIds[0].type)
        assertEquals("a", wordIds[0].name)
        assertEquals(1, result.namesDictionary.size)
    }

    @Test
    fun testVariableDeclaration() {
        val env = prepareTokenizer()
        // expected
        // <float><max2><=><max><(><1><,><2><)>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                float max2=max(1,2)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(9, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals(Type.DIGIT, wordIds[5].type)
        assertEquals(Type.DIGIT, wordIds[7].type)
        // check dictionaries
        assertEquals(2, result.digitsDictionary.size)
        assertEquals(1, result.namesDictionary.size)
        assertEquals(0, result.stringsDictionary.size)
    }

    @Test
    fun testDigitWithPoint() {
        val env = prepareTokenizer()
        // expected
        // <float><max2><=><max><(><1><,><2><)>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                float max2=max(1.2,2.341)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
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
        assertEquals(2, result.digitsDictionary.size)
        assertEquals("1.2", result.digitsDictionary[0].name)
    }

    @Test
    fun testDigitWithPointNegative() {
        val env = prepareTokenizer()
        // expected
        // <float><max2><=><max><(><1><,><-><2.341><)>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                float max2=max(1.2,-2.341,1.2)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(12, wordIds.size)
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
        assertEquals(2, result.digitsDictionary.size)
        assertEquals("2.341", result.digitsDictionary[1].name)
    }

    @Test
    fun testDigitHex() {
        val env = prepareTokenizer()
        // expected
        // <int><a><=><0x1234>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                int a=0xFAB
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(4, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.DIGIT, wordIds[3].type)
        assertEquals(DIGIT_BASE, wordIds[3].id)

        assertEquals(1, result.digitsDictionary.size)
        assertEquals("0xFAB", result.digitsDictionary[0].name)
    }

    @Test
    fun testMultiline() {
        val env = prepareTokenizer()
        // expected
        // <#><pragma><once><namespace><name qwe>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                #pragma once

                namespace com
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
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
        val env = prepareTokenizer()
        // expected
        // <namespace><name qwe><operator ::><name goldman><operator ::><name dt1>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                namespace com::goldman::dt1
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(6, wordIds.size, "Wrong word ids number")
    }

    @Test
    fun testParseStringAssignement() {
        val env = prepareTokenizer()
        // expected
        // <name a><operator =><literal "Simple string"><operator ;>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                a = "Simple string";
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(4, wordIds.size, "Wrong word ids number")
        assertEquals(Type.NAME, wordIds[0].type)
        assertEquals(Type.OPERATOR, wordIds[1].type)
        assertEquals(Type.STRING_LITERAL, wordIds[2].type)
        assertEquals(STRINGS_BASE, wordIds[2].id)
    }

    @Test
    fun testAllDictionaries() {
        val env = prepareTokenizer()
        // expected
        // <name a><operator =><keyword max>(operator (><digit 10><op ,><literal "qwe"><op ,><name b><op )>
        val result = env.tokenizer(
            buffer = SourceBuffer(
                """
                a = max(10,"qwe",b)
            """.trimIndent()
            ),
            dictionaries = TestDictionary.dictionaries,
            namesDictionary = env.namesDictionaryRepo,
            digitsDictionary = env.digitsDictionaryRepo,
            stringLiteralsDictionary = env.stringLiteralsDictionaryRepo,
        )
        val wordIds = result.words
        assertEquals(10, wordIds.size, "Wrong word ids number")
        assertEquals(Type.NAME, wordIds[0].type)
        assertEquals(Type.OPERATOR, wordIds[1].type)
        assertEquals(Type.KEYWORD, wordIds[2].type)
        assertEquals(Type.OPERATOR, wordIds[3].type)
        assertEquals(Type.DIGIT, wordIds[4].type)
        assertEquals(DIGIT_BASE, wordIds[4].id)
        assertEquals(Type.OPERATOR, wordIds[5].type)
        assertEquals(Type.STRING_LITERAL, wordIds[6].type)
        assertEquals(STRINGS_BASE, wordIds[6].id)
        assertEquals(Type.OPERATOR, wordIds[7].type)
        assertEquals(Type.NAME, wordIds[8].type)
        assertEquals(NAME_BASE, wordIds[8].id)
        assertEquals(Type.OPERATOR, wordIds[9].type)
        // check dictionaries
        assertEquals(1, result.stringsDictionary.size)
        assertEquals(1, result.digitsDictionary.size)
        assertEquals(1, result.namesDictionary.size)
        // check ids
        assertEquals(STRINGS_BASE, result.stringsDictionary[0].id)
        assertEquals(DIGIT_BASE, result.digitsDictionary[0].id)
        assertEquals(NAME_BASE, result.namesDictionary[0].id)
    }

    // next test
//    a = "Simple string";
//    b = 10;
    // check dictionaries names = a,b, digits = 10, string = "Simpel string"

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