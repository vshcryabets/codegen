package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import generators.obj.input.Leaf
import generators.obj.input.Namespace
import java.io.File

class JsonTreeWritterImpl : TreeWritter {
    override fun storeTree(filePath: String, tree: Leaf) {
        val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        val module = SimpleModule()
        module.addSerializer(DataType::class.java, DataTypeSerializer())
        module.addSerializer(DataValue::class.java, DataValueSerializer())
        mapper.registerModule(module)

        // store input tree
        var outputFile = File(filePath)
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        mapper.writeValue(outputFile, tree);
    }
}