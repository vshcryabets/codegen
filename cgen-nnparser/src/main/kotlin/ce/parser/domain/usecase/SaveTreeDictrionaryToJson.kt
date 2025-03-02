package ce.parser.domain.usecase

import ce.parser.domain.dictionaries.TreeNodeData
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class SaveTreeDictrionaryToJson: SaveTreeDictionary {
    override fun save(file: File, dictionary: Map<String, TreeNodeData>) {
        val mapper = ObjectMapper()
        val writer = OutputStreamWriter(FileOutputStream(file))
//        mapper.writeValue(writer, dictionary)
        mapper.writerWithDefaultPrettyPrinter().writeValue(writer, dictionary)
        writer.close()
    }
}