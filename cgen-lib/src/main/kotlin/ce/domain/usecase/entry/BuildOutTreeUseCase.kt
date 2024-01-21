package ce.domain.usecase.entry

import ce.defs.Target
import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.GeneratorsRepo
import generators.obj.input.Node
import javax.inject.Inject

interface BuildOutTreeUseCase {
    operator fun invoke(projectFile: String, inTreeFile: String, outTreeFile: String, target: Target)
}

class BuildOutTreeUseCaseImpl @Inject constructor(
    private val loadInTreeUseCase : LoadAstTreeUseCase,
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeOutTreeUseCase : StoreOutTreeUseCase,
    private val transformInTreeToOutTreeUseCase : TransformInTreeToOutTreeUseCase,
): BuildOutTreeUseCase {
    override operator fun invoke(projectFile: String, inTreeFile: String, outTreeFile: String, target: Target) {
        val generatorsRepo = GeneratorsRepo(getProjectUseCase(projectFile))
        val tree = loadInTreeUseCase(inTreeFile)
        val outTree = transformInTreeToOutTreeUseCase(tree as Node, generatorsRepo.get(target))
        storeOutTreeUseCase(outTreeFile, outTree)
    }
}