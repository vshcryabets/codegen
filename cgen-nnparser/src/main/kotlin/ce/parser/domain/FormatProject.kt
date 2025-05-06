package ce.parser.domain

import ce.parser.domain.dictionaries.tree.Dictionary

data class FormatProject(
    val name: String,
    val type: String,
    val author: String,
    val process: String,
    val dictionary: Dictionary,
    val samples: List<List<Int>>
)