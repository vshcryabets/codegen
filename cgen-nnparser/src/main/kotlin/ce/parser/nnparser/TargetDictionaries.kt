package ce.parser.nnparser

data class TargetDictionaries(
    val operators: WordDictionary,
    val stdlibs: WordDictionary,
    val thirdlibs: WordDictionary,
    val projectlibs: WordDictionary,
    val map: Map<Type, WordDictionary>,
) {
    val keywords: WordDictionary
        get() = map[Type.KEYWORD]!!
}