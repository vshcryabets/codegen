package ce.parser

class WordDictionary(
    wordsList: List<Word>,
) {
    private val sortedByLengthDict: List<Word>
    val dictionary: MutableMap<Int, Word> = mutableMapOf()
    val reverse: MutableMap<String, Int> = mutableMapOf()
    var maxId: Int = 0

    init {
        wordsList.forEach {
            if (it.id > maxId) maxId = it.id
            dictionary[it.id] = it
            reverse[it.name] = it.id
        }
        sortedByLengthDict = wordsList.sortedBy { it.name.length }
    }

    fun addWord(word: Word): Word {
        println("Add word $word")
        maxId++
        val newWord = word.copy(id = maxId)
        dictionary[maxId] = newWord
        reverse[word.name] = maxId
        return newWord
    }
}