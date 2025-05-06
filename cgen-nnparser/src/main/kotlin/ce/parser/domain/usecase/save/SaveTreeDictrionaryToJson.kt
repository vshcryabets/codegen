package ce.parser.domain.usecase.save

import ce.parser.domain.dictionaries.tree.Dictionary
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class SaveTreeDictrionaryToJson: SaveTreeDictionary {
    override fun save(file: File, dictionary: Dictionary) {
        val mapper = ObjectMapper()
        val writer = OutputStreamWriter(FileOutputStream(file))
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, dictionary.map)
        writer.close()
    }
}