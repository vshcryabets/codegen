package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.abstractSyntaxTree.Leaf

class LoadXmlTreeUseCase {
    private val reader = XmlTreeReader()

    fun fromFile(inputFile : String) : Leaf {
        return reader.load(inputFile)
    }

    fun fromString(data: String): Leaf {
        return reader.loadFromString(data)
    }
}