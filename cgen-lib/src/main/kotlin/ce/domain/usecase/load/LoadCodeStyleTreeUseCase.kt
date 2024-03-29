package ce.domain.usecase.load

import ce.treeio.XmlTreeReader
import generators.obj.out.CodeStyleOutputTree

class LoadCodeStyleTreeUseCase {
    operator fun invoke(inputFile : String) : CodeStyleOutputTree {
        val reader = XmlTreeReader()
        return reader.load(inputFile) as CodeStyleOutputTree
    }
}