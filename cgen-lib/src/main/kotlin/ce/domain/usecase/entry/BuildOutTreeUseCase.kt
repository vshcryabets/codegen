package ce.domain.usecase.entry

import ce.defs.Target
import ce.defs.domain.DirsConfiguration
import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.CodestyleRepoImpl
import ce.repository.GeneratorsRepo
import generators.obj.input.Node
import javax.inject.Inject

interface BuildOutTreeUseCase {
    operator fun invoke(projectFile: String, inTreeFile: String,
                        outTreeFile: String, target: Target,
                        dirsConfiguration: DirsConfiguration)
}

class BuildOutTreeUseCaseImpl @Inject constructor(
    private val loadInTreeUseCase : LoadAstTreeUseCase,
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeOutTreeUseCase : StoreOutTreeUseCase,
    private val transformInTreeToOutTreeUseCase : TransformInTreeToOutTreeUseCase,
): BuildOutTreeUseCase {
    override operator fun invoke(projectFile: String, inTreeFile: String,
                                 outTreeFile: String, target: Target,
                                 dirsConfiguration: DirsConfiguration) {
        val project = getProjectUseCase(projectFile, dirsConfiguration)
        val codeStyleRepo = CodestyleRepoImpl(project)
        val generatorsRepo = GeneratorsRepo(
            project = project,
            codestylesRepo = codeStyleRepo
        )
        val tree = loadInTreeUseCase(inTreeFile)
        val outTree = transformInTreeToOutTreeUseCase(tree as Node, generatorsRepo.get(target))
        storeOutTreeUseCase(outTreeFile, outTree)
    }
}