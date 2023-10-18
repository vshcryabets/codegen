package ce.parser

data class TargetDictionaries(
    val core: Map<Int, Word>,
    val stdlibs: Map<Int, Word>,
    val thirdlibs: Map<Int, Word>,
    val projectlibs: Map<Int, Word>,
)