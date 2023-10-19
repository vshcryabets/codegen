package ce.parser

data class WordDictionary(
    val dictionary: MutableMap<Int, Word>,
    val reverse: MutableMap<String, Int>,
    val maxId: Int
)