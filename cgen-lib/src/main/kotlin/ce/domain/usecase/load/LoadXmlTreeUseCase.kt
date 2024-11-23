package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.input.Leaf
import generators.obj.out.AstTree

class LoadXmlTreeUseCase {
    private val reader = XmlTreeReader()

    fun fromFile(inputFile : String) : Leaf {
        return reader.load(inputFile)
    }

    fun fromString(data: String): Leaf {
        return reader.loadFromString(data)
    }
}