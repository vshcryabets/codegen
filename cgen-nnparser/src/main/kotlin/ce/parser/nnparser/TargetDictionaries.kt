package ce.parser.nnparser

data class TargetDictionaries(
    val keywords: WordDictionary,
    val operators: WordDictionary,
    val spaces: WordDictionary,
    val comments: WordDictionary,
    val stdlibs: WordDictionary,
    val thirdlibs: WordDictionary,
    val projectlibs: WordDictionary,
)