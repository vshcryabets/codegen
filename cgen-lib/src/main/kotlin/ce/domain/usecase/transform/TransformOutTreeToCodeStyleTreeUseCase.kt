package ce.domain.usecase.transform

import ce.formatters.PrepareCodeStyleTreeUseCase
import generators.obj.out.CodeStyleOutputTree
import generators.obj.out.OutputTree

@Deprecated("Please use PrepareCodeStyleTreeUseCase")
class TransformOutTreeToCodeStyleTreeUseCase {
    operator fun invoke(outTree: OutputTree,
                        prepareCodeStyleTreeUseCase: PrepareCodeStyleTreeUseCase) : CodeStyleOutputTree {
        return prepareCodeStyleTreeUseCase.prepareCodeStyleTree(outTree)
    }
}