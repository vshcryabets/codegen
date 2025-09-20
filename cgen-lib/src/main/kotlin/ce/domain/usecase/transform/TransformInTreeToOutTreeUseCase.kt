package ce.domain.usecase.transform

import generators.obj.MetaGenerator
import generators.obj.abstractSyntaxTree.Node
import generators.obj.syntaxParseTree.OutputTree

class TransformInTreeToOutTreeUseCase {
    operator fun invoke(inTree: Node, metaGenerator: MetaGenerator) : OutputTree {
        return metaGenerator.translateToOutTree(inTree)
    }
}