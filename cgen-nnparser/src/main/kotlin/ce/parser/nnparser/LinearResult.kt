package ce.parser.nnparser

import ce.parser.nnparser.Word

data class LinearResult(
    val wordsMap: Map<Int, WordItem> = emptyMap(),
    val literalsMap: Map<Int, Word> = emptyMap(),
    val namesMap: Map<Int, Word> = emptyMap(),
    val digits: Map<Int, Word> = emptyMap(),
    val tokens: List<Int> = emptyList()
)
