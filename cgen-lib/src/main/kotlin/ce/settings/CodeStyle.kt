package ce.settings

data class CodeStyle(
    var newLinesBeforeClass : Int = 1,
    var tabSize : Int = 4,
    var preventEmptyBlocks: Boolean = false,
)