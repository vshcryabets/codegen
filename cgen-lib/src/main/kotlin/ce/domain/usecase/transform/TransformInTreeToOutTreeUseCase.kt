package ce.domain.usecase.transform

import ce.defs.namespaceMap
import generators.obj.MetaGenerator
import generators.obj.input.Node
import generators.obj.out.ProjectOutput

class TransformInTreeToOutTreeUseCase {
    operator fun invoke(inTree: Node, metaGenerator: MetaGenerator) : ProjectOutput {
        return metaGenerator.translateToOutTree(inTree, namespaceMap)
    }
}