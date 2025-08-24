package ce.domain.usecase.store

import ce.treeio.XmlInTreeWritterImpl
import generators.obj.abstractSyntaxTree.Node
import java.io.File

class StoreAstTreeUseCase {
    operator fun invoke(outputFileName : String, tree: Node)  {
        val treeWritter = XmlInTreeWritterImpl()
        val outputFile = File(outputFileName)
        outputFile.parentFile.mkdirs()
        treeWritter.storeTree(outputFile.absolutePath, tree)
    }
}