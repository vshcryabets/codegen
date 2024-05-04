package ce.parser.domain.dictionaries

import ce.parser.nnparser.Type
import ce.parser.nnparser.Word
import ce.parser.nnparser.WordItem

class NamesDictionary(
    private val startId: Int,
    private val maxId: Int,
    private val type: Type
) {
    var currentId: Int = startId
    val knownWords = mutableMapOf<String, WordItem>()

    fun search(name: String): WordItem {
        if (knownWords.containsKey(name)) {
            return knownWords[name]!!
        } else {
            val result = Word(name = name, type = type, id = currentId)
            currentId++
            if (currentId > maxId) {
                throw IllegalStateException("Not enough dictionary capacity. currentId= $currentId, maxId =$maxId")
            }
            knownWords[name] = result
            return result
        }
    }

    fun exportToWordsList(): List<WordItem> = knownWords.values.toList()

    fun clear() {
        currentId = startId
        knownWords.clear()
    }
}