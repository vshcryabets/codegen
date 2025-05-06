package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.tree.Dictionary
import java.io.File

interface LoadTreeDictionary {
    fun load(file: File) : Dictionary
}