package ce.parser.domain

import ce.parser.nnparser.*

object TestDictionary {
    fun getDict(strings: List<String>, baseId: Int, type: Type): WordDictionary {
        var id = baseId
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

    val keywordDict = getDict(listOf("max", "float", "int", "pragma", "once", "namespace"), 100, Type.KEYWORD)
    val operatorsDict =
        getDict(listOf("(", ")", "=", "->", ",", "-", ">", "+", "#", ":", "::", ";"), 200, Type.OPERATOR)
    val spaces = getDict(listOf(" ", "\t", "\n"), 1000, Type.SPACES)
    val comments = WordDictionary(
        listOf(
//        Comment(name = "//", oneLineComment = true, id = 2000, type = Type.COMMENTS),
//        Comment(name = "/*", oneLineComment = false, multilineCommentEnd = "*/", id = 2001, type = Type.COMMENTS),
            RegexWord(name = "//(.*)", id = 2000, type = Type.COMMENTS),
            RegexWord(name = "/\\*.*\\*/", id = 2001, type = Type.COMMENTS),
        ),
        sortBySize = false
    )
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

}