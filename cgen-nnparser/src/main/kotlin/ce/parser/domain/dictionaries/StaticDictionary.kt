package ce.parser.domain.dictionaries

import ce.parser.nnparser.Word
import ce.parser.nnparser.WordItem

class StaticDictionary(
    wordsList: List<WordItem>,
    sortBySize: Boolean = true
) {
    val sortedByLengthDict: List<WordItem>

    init {
        if (sortBySize) {
            sortedByLengthDict = wordsList.sortedByDescending { it.name.length }
        } else {
            sortedByLengthDict = wordsList
        }
    }

    fun search(name: String): WordItem? =
        sortedByLengthDict.firstOrNull {
            it.name.equals(name)
        }
}