package ce.parser

data class TargetDictionaries(
    val core: WordDictionary,
    val stdlibs: WordDictionary,
    val thirdlibs: WordDictionary,
    val projectlibs: WordDictionary,
)