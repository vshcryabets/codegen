package ce.domain.usecase.entry

import ce.defs.Target
import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreCodeStyleTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.GeneratorsRepo

class BuildCodeStyleTreeUseCase(
    private val loadInTreeUseCase : LoadAstTreeUseCase,
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeCodeStyleTreeUseCase: StoreCodeStyleTreeUseCase,
    private val transformInTreeToOutTreeUseCase : TransformInTreeToOutTreeUseCase,
) {
    operator fun invoke(projectFile: String, inTreeFile: String, codeStyleTreeFile: String, target: Target) {
        val generatorsRepo = GeneratorsRepo(getProjectUseCase(projectFile))
        val outTree = loadInTreeUseCase(inTreeFile)
        val codeStyleTree = transformInTreeToOutTreeUseCase(outTree, generatorsRepo.get(target))
        storeCodeStyleTreeUseCase(codeStyleTreeFile, codeStyleTree)
    }
}