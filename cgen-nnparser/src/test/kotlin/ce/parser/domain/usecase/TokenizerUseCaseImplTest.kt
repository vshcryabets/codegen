package ce.parser.domain.usecase

import ce.parser.nnparser.Comment
import ce.parser.nnparser.RegexWord
import ce.parser.nnparser.TargetDictionaries
import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordDictionary
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

fun getDict(strings: List<String>, basiId: Int, type: Type): WordDictionary {
    var id = basiId
    return WordDictionary(
        strings.map {
            Word(
                name = it,
                type = type,
                id = id++
            )
        }
    )
}

class TokenizerUseCaseImplTest {

    val keywordDict = getDict(listOf("max", "float", "int","pragma","once","namespace"), 100, Type.KEYWORD)
    val operatorsDict = getDict(listOf("(", ")", "=", "->", ",", "-", ">", "+","#"), 200, Type.OPERATOR)
    val spaces = getDict(listOf(" ", "\t", "\n"), 1000, Type.SPACES)
    val comments = WordDictionary(listOf(
        Comment(name = "//", oneLineComment = true, id = 2000, type = Type.COMMENTS),
        Comment(name = "/*", oneLineComment = false, multilineCommentEnd = "*/", id = 2001, type = Type.COMMENTS),
    ))
    val dictionaries = TargetDictionaries(
        map = mapOf(
            Type.SPACES to spaces,
            Type.COMMENTS to comments,
            Type.KEYWORD to keywordDict,
            Type.DIGIT to WordDictionary(
                listOf(
                    RegexWord(name = "0x[\\dABCDEFabcdef]+", id = 3000, type = Type.DIGIT),
                    RegexWord(name = "\\d+\\.*\\d*f*", id = 3001, type = Type.DIGIT)
                ),
                sortBySize = false
            ),
            Type.OPERATOR to operatorsDict
        )
    )

    @Test
    fun testSpaces() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <OP ->
        val result = tokenizer(
            text = "\t  \t- ",
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
        assertEquals(1, wordIds.size)
        assertEquals(Type.OPERATOR, wordIds[0].type)
        assertEquals("-", wordIds[0].name)

        // expected
        // <KEY max><OP +>
        val result2 = tokenizer(
            text = "  max +",
            dictinaries = dictionaries,
        )
        val wordIds2 = result2.wordIds
        assertEquals(2, wordIds2.size)
        assertEquals(Type.KEYWORD, wordIds2[0].type)
        assertEquals("max", wordIds2[0].name)
        assertEquals(Type.OPERATOR, wordIds2[1].type)
        assertEquals("+", wordIds2[1].name)
    }

    @Test
    fun testName() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <Name x><=><max><(><)>
        val result = tokenizer(
            text = "x = max\n()",
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
        assertEquals(5, wordIds.size)
        assertEquals(Type.NAME, wordIds[0].type)
        assertEquals("x", wordIds[0].name)
        assertEquals(Type.OPERATOR, wordIds[1].type)
        assertEquals("=", wordIds[1].name)
    }

    @Test
    fun testName2() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <KW float><Name max2><=><max><(><)>
        val result = tokenizer(
            text = "float max2=max()",
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
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
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <Comment comment><Name a><=><max><(><)>
        val result = tokenizer(
            text = """
                //1+comment
                a=max()
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
        assertEquals(6, wordIds.size)
        assertEquals(Type.COMMENTS, wordIds[0].type)
        assertEquals("1+comment", wordIds[0].name)
    }

    @Test
    fun testCommentLine2() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <Name a><=><max><(><)><Comment comment>
        val result = tokenizer(
            text = """
                a=max() //1+comment
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
        assertEquals(6, wordIds.size)
        assertEquals(Type.NAME, wordIds[0].type)
        assertEquals("a", wordIds[0].name)
    }

    @Test
    fun testVariableDeclaration() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <float><max2><=><max><(><1><,><2><)>
        val result = tokenizer(
            text = """
                float max2=max(1,2)
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
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
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <float><max2><=><max><(><1><,><2><)>
        val result = tokenizer(
            text = """
                float max2=max(1.2,2.341)
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
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
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <float><max2><=><max><(><1><,><-><2><)>
        val result = tokenizer(
            text = """
                float max2=max(1.2,-2.341)
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
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
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <int><a><=><0x1234>
        val result = tokenizer(
            text = """
                int a=0xFAB
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
        assertEquals(4, wordIds.size)
        assertEquals(Type.KEYWORD, wordIds[0].type)
        assertEquals(Type.NAME, wordIds[1].type)
        assertEquals(Type.OPERATOR, wordIds[2].type)
        assertEquals(Type.DIGIT, wordIds[3].type)
    }

    @Test
    fun testMultiline() {
        val tokenizer = TokenizerUseCaseImpl()
        // expected
        // <#><pragma><once><namecpase><name qwe>
        val result = tokenizer(
            text = """
                #pragma once

                namespace com
            """.trimIndent(),
            dictinaries = dictionaries,
        )
        val wordIds = result.wordIds
        assertEquals(5, wordIds.size)
        assertEquals(Type.OPERATOR, wordIds[0].type)
        assertEquals(Type.KEYWORD, wordIds[1].type)
        assertEquals(Type.KEYWORD, wordIds[2].type)
        assertEquals(Type.KEYWORD, wordIds[3].type)
        assertEquals(Type.NAME, wordIds[4].type)
    }

    // tests
    // check ids
    // check debugFindings
    // float max2=max(1,-2)
    // float max2=max(.5,.25)
    // qwe->varX
    // qwe-varX
    // qwe>varX
    // abc = "String literal"
}