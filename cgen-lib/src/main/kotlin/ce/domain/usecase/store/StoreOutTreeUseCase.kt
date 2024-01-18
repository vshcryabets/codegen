package ce.domain.usecase.store

import ce.treeio.XmlInTreeWritterImpl
import generators.obj.input.Node
import java.io.File

class StoreOutTreeUseCase {
    operator fun invoke(outputFile : String, tree: Node)  {
        val treeWritter = XmlInTreeWritterImpl()
        val outputFile = File(outputFile)
        outputFile.parentFile?.mkdirs()
        treeWritter.storeTree(outputFile.absolutePath, tree)
    }
}