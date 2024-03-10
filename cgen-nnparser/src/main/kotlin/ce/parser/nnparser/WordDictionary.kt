package ce.parser.nnparser

class WordDictionary(
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
}