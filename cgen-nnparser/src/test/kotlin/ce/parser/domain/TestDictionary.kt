package ce.parser.domain

import ce.parser.domain.dictionaries.*
import ce.parser.nnparser.*

object TestDictionary {
    val DIGIT_BASE = 13000
    val NAME_BASE = 14000
    val STRINGS_BASE = 15000

    fun getDict(strings: List<String>, baseId: Int, type: Type): StaticDictionary {
        var id = baseId
        return StaticDictionary(
            strings.map {
                Word(
                    name = it,
                    type = type,
                    id = id++
                )
            }
        )
    }

    val keywordDict = getDict(listOf("max", "float", "int", "pragma", "once", "namespace", "add"), 100, Type.KEYWORD)
    val operatorsDict =
        getDict(listOf("(", ")", "=", "->", ",", "-", ">", "+", "#", ":", "::", ";",".","\""), 200, Type.OPERATOR)
    val spaces = getDict(listOf(" ", "\t", "\n"), 1000, Type.SPACES)
    val comments = StaticDictionary(
        listOf(
            RegexWord(name = "//(.*)", id = 2000, type = Type.COMMENTS),
            RegexWord(name = "/\\*.*\\*/", id = 2001, type = Type.COMMENTS),
        ),
        sortBySize = false
    )
    val dictionaries = StaticDictionariesImpl(
        spaces = spaces,
        comments = comments,
        keywords = keywordDict,
        digits = StaticDictionary(
            listOf(
                RegexWord(name = "0x[\\dABCDEFabcdef]+", id = 3000, type = Type.DIGIT),
                RegexWord(name = "\\d+\\.*\\d*f*", id = 3001, type = Type.DIGIT)
            ),
            sortBySize = false
        ),
        operators = operatorsDict,
        strings = StaticDictionary(
            listOf(
                ClikeLiteralWord(name = "SimpleString", id = 4000, type = Type.STRING_LITERAL)
            ),
            sortBySize = false
        )
    )

    fun prepareDynamicDictionaries(): DynamicDictionaries =
        DynamicDictionariesImpl(
            digitsDictionary = NamesDictionary(
                startId = DIGIT_BASE,
                maxId = NAME_BASE,
                type = Type.DIGIT
            ),
            namesDictionary = NamesDictionary(
                startId = NAME_BASE,
                maxId = STRINGS_BASE,
                type = Type.NAME
            ),
            stringLiteralsDictionary = NamesDictionary(
                startId = STRINGS_BASE,
                maxId = STRINGS_BASE + 1000,
                type = Type.STRING_LITERAL
            ),
        )
}