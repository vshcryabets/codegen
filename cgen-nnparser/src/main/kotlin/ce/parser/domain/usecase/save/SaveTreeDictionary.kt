package ce.parser.domain.usecase.save

import ce.parser.domain.dictionaries.tree.Dictionary
import java.io.File

interface SaveTreeDictionary {
    fun save(file: File, dictionary: Dictionary)
}