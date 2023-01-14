package ce.domain.usecase.store

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.globRootNamespace
import ce.settings.Project
import ce.treeio.DataTypeSerializer
import ce.treeio.DataValueSerializer
import ce.treeio.XmlInTreeWritterImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import generators.obj.input.Node
import java.io.File
import java.io.FileInputStream

class StoreOutTreeUseCase {
    operator fun invoke(outputFile : String, tree: Node)  {
        val treeWritter = XmlInTreeWritterImpl()
        val outputFile = File(outputFile)
        outputFile.parentFile?.mkdirs()
        treeWritter.storeTree(outputFile.absolutePath, tree)
    }
}