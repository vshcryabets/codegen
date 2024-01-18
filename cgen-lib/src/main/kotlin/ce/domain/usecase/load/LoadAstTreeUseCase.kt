package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.out.AstTree

class LoadAstTreeUseCase {
    operator fun invoke(inputFile : String) : AstTree {
        val reader = XmlTreeReader()
        return reader.load(inputFile) as AstTree
    }
}