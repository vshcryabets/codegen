package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.TreeNodeData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

class LoadTreeDictionaryFromJson(
    private val objectMapper: ObjectMapper
): LoadTreeDictionary {
    override fun load(file: File): Map<String, TreeNodeData> {
        return if (file.exists()) {
            objectMapper.readValue(
                file,
                objectMapper.typeFactory.constructMapType(Map::class.java, String::class.java, TreeNodeData::class.java)
            )
        } else {
            emptyMap()
        }
    }
}