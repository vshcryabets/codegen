package ce.domain.usecase.transform

import generators.obj.MetaGenerator
import generators.obj.input.Node
import generators.obj.out.OutputTree

class TransformInTreeToOutTreeUseCase {
    operator fun invoke(inTree: Node, metaGenerator: MetaGenerator) : OutputTree {
        return metaGenerator.translateToOutTree(inTree)
    }
}