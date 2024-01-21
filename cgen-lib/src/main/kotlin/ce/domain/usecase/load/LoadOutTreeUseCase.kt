package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.out.OutputTree

class LoadOutTreeUseCase {
    operator fun invoke(inputFile : String) : OutputTree {
        val reader = XmlTreeReader()
        return reader.load(inputFile) as OutputTree
    }
}