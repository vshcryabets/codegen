package ce.parser.nnparser

data class TargetDictionaries(
    val map: Map<Type, WordDictionary>,
) {
    val keywords: WordDictionary
        get() = map[Type.KEYWORD]!!

    val operators: WordDictionary
        get() = map[Type.OPERATOR]!!
}