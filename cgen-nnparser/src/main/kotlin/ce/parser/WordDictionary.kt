package ce.parser

data class WordDictionary(
    val dictionary: MutableMap<Int, Word>,
    val reverse: MutableMap<String, Int>,
    var maxId: Int
) {
    fun addWord(word: Word): Int {
        println("Add word $word")
        maxId++
        dictionary[maxId] = word
        reverse[word.name] = maxId
        return maxId
    }
}