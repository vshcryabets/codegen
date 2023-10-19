package ce.parser

data class LinearResult(
    val wordsMap: Map<Int, Word> = emptyMap(),
    val literalsMap: Map<Int, Literal> = emptyMap(),
    val namesMap: Map<Int, Name> = emptyMap(),
    val digits: Map<Int, Digit> = emptyMap(),
    val tokens: List<Int> = emptyList()
)
