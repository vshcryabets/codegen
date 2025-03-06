package ce.parser.domain.dictionaries

data class FormatProject(
    val name: String,
    val process: String,
    val dictionary: Map<String, TreeNodeData>,
    val samples: List<List<Int>>
)