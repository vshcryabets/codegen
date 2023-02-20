package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.input.Leaf

class LoadInTreeUseCase {
    operator fun invoke(inputFile : String) : Leaf {
        val reader = XmlTreeReader()
        return reader.load(inputFile)
    }
}