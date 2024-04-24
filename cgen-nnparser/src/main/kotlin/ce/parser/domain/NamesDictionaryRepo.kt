package ce.parser.domain

import ce.parser.nnparser.Type
import ce.parser.nnparser.Word

class NamesDictionaryRepo(
    startId: Int,
    private val type: Type
) {
    var currentId: Int = startId
    val knownWords = mutableMapOf<String, Word>()

    fun search(name: String): Word {
        if (knownWords.containsKey(name)) {
            return knownWords[name]!!
        } else {
            val result = Word(name = name, type = type, id = currentId)
            currentId++
            knownWords[name] = result
            return result
        }
    }

    fun exportToWordsList(): List<Word> = knownWords.values.toList()
}