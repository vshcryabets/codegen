package ce.parser.domain.usecase

import ce.parser.domain.TestDictionary
import ce.parser.domain.dictionaries.DynamicDictionaries
import ce.parser.nnparser.SourceBuffer
import ce.parser.nnparser.Type
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MetaTokenizerUseCaseImplTest {
    data class PreparedEnv(
        val tokenizer: TokenizerUseCase,
        val dynamicDictionaries: DynamicDictionaries,
    )

    fun prepareTokenizer(): PreparedEnv {
        val checkStringInDictionary = CheckStringInDictionaryImpl()
        return PreparedEnv(
            tokenizer = MetaTokenizerUseCaseImpl(
                checkStringInDictionaryUseCase = checkStringInDictionary
            ),
            dynamicDictionaries = TestDictionary.prepareDynamicDictionaries()
        )
    }

    @Test
    fun metaNamespace() {
        val env = prepareTokenizer()
        // expected
        // <keyword namespace><operator (><operator "><string a>
        // <operator .><string b><operator .><string c><operator ">
        // <operator )>
        val src = SourceBuffer("namespace(\"a.b.c\")")
        val result = env.tokenizer(buffer = src,
            dynamicDictionaries = env.dynamicDictionaries,
            dictionaries = TestDictionary.metaDictionaries,
            debugFindings = true
            )
        assertEquals(10, result.words.size)
    }

    @Test
    fun metaAdd() {
        val env = prepareTokenizer()
        // expected
        // <keyword add><operator (><operator "><name varName>
        // <operator "><operator )>
        val src = SourceBuffer("add(\"varName\")")
        val result = env.tokenizer(buffer = src,
            dynamicDictionaries = env.dynamicDictionaries,
            dictionaries = TestDictionary.metaDictionaries,
            debugFindings = true
        )
        assertEquals(6, result.words.size)
        assertEquals(Type.KEYWORD, result.words[0].type)
        assertEquals(Type.OPERATOR, result.words[1].type)
        assertEquals(Type.OPERATOR, result.words[2].type)
        assertEquals(Type.NAME, result.words[3].type)
        assertEquals(Type.OPERATOR, result.words[4].type)
        assertEquals(Type.OPERATOR, result.words[5].type)

    }

    @Test
    fun metaAddBlockComment() {
        val env = prepareTokenizer()
        // expected
        // <keyword addBlockComment><operator (><operator "><comment comment>
        // <operator "><operator )>
        val src = SourceBuffer("addBlockComment(\"comment\")")
        val result = env.tokenizer(buffer = src,
            dynamicDictionaries = env.dynamicDictionaries,
            dictionaries = TestDictionary.metaDictionaries,
            debugFindings = true
        )
        assertEquals(1, result.words.size)
        assertEquals(Type.COMMENTS, result.words[0].type)
    }
}