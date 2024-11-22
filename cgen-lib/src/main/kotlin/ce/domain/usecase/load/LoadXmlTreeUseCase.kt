package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.input.Leaf
import generators.obj.out.AstTree

class LoadXmlTreeUseCase {
    operator fun invoke(inputFile : String) : Leaf {
        val reader = XmlTreeReader()
        return reader.load(inputFile)
    }
}