package ce.parser.nnparser

class WordDictionary(
    wordsList: List<WordItem>,
    private val sortBySize: Boolean = true
) {
    val sortedByLengthDict: List<WordItem>
    val dictionary: MutableMap<Int, WordItem> = mutableMapOf()
    val reverse: MutableMap<String, Int> = mutableMapOf()
    var maxId: Int = 0

    init {
        wordsList.forEach {
            if (it.id > maxId) maxId = it.id
            dictionary[it.id] = it
            reverse[it.name] = it.id
        }
        if (sortBySize) {
            sortedByLengthDict = wordsList.sortedBy { it.name.length }
        } else {
            sortedByLengthDict = wordsList
        }
    }

    fun addWord(word: WordItem): WordItem {
        println("Add word $word")
        maxId++
        val newWord = Word(
            name = word.name,
            id = maxId)
        dictionary[maxId] = newWord
        reverse[word.name] = maxId
        return newWord
    }
}