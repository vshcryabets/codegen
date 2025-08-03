package ce.domain.usecase.entry

import ce.defs.domain.DirsConfiguration
import ce.domain.usecase.load.LoadOutTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreCodeStyleTreeUseCase
import ce.domain.usecase.transform.TransformOutTreeToCodeStyleTreeUseCase
import ce.formatters.PrepareCodeStyleTreeUseCaseImpl
import ce.repository.CodestyleRepoImpl
import ce.repository.GeneratorsRepo

class BuildCodeStyleTreeUseCase(
    private val loadOutTreeUseCase : LoadOutTreeUseCase,
    private val loadProjectUseCase : LoadProjectUseCase,
    private val storeCodeStyleTreeUseCase: StoreCodeStyleTreeUseCase,
    private val transformOutTreeToCodeStyleTreeUseCase : TransformOutTreeToCodeStyleTreeUseCase,
) {
    operator fun invoke(projectFile: String,
                        outTreeFile: String,
                        codeStyleTreeFile: String,
                        dirsConfiguration: DirsConfiguration) {
        val project = loadProjectUseCase(projectFile, dirsConfiguration)
        val codeStyleRepo = CodestyleRepoImpl(project)
        val generatorsRepo = GeneratorsRepo(project, codeStyleRepo)
        val outTree = loadOutTreeUseCase(outTreeFile)
        val codeStyleTree = transformOutTreeToCodeStyleTreeUseCase(outTree,
            PrepareCodeStyleTreeUseCaseImpl(generatorsRepo.getFormatter(outTree.target)))
        storeCodeStyleTreeUseCase(codeStyleTreeFile, codeStyleTree)
    }
}