package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.TreeNodeData
import java.io.File

interface SaveTreeDictionary {
    fun save(file: File, dictionary: Map<String, TreeNodeData>)
}