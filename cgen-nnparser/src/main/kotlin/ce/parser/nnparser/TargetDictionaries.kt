package ce.parser.nnparser

data class TargetDictionaries(
    val map: Map<Type, WordDictionary>,
) {
    val keywords: WordDictionary
        get() = map[Type.KEYWORD]!!

    val operators: WordDictionary
        get() = map[Type.OPERATOR]!!

    val digits: WordDictionary
        get() = map[Type.DIGIT]!!

    val comments: WordDictionary
        get() = map[Type.COMMENTS]!!
}