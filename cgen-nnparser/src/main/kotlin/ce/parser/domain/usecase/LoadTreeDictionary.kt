package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.TreeNodeData
import java.io.File

interface LoadTreeDictionary {
    fun load(file: File) : Map<String, TreeNodeData>
}