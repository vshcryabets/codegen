package ce.domain.usecase.transform

import generators.obj.MetaGenerator
import generators.obj.out.CodeStyleOutputTree
import generators.obj.out.OutputTree

class TransformOutTreeToCodeStyleTreeUseCase {
    operator fun invoke(outTree: OutputTree, metaGenerator: MetaGenerator) : CodeStyleOutputTree {
        return metaGenerator.prepareCodeStyleTree(outTree)
    }
}