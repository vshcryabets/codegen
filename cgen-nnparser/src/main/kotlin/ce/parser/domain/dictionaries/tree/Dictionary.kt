package ce.parser.domain.dictionaries.tree

data class Dictionary (
    val map: MutableMap<String, TreeNodeData>,
    var maxId: Int = 1
)
