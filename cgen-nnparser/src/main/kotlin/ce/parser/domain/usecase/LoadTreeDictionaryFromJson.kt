package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.tree.Dictionary
import ce.parser.domain.dictionaries.tree.TreeNodeData
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class LoadTreeDictionaryFromJson(
    private val objectMapper: ObjectMapper
): LoadTreeDictionary {
    override fun load(file: File): Dictionary {
        val dictionary = Dictionary(
            map = mutableMapOf()
        )
        if (file.exists()) {
            val map : Map<String,TreeNodeData> = objectMapper.readValue(
                file,
                objectMapper.typeFactory.constructMapType(Map::class.java, String::class.java, TreeNodeData::class.java)
            )
            map.forEach { (key, value) ->
                dictionary.maxId = maxOf(dictionary.maxId, value.openId, value.closeId)
                dictionary.map[key] = value
            }
        }
        return dictionary
    }
}