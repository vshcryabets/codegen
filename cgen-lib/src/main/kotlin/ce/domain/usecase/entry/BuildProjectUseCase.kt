package ce.domain.usecase.entry

import ce.defs.domain.DirsConfiguration
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreAstTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.CodestyleRepoImpl
import ce.repository.GeneratorsRepo
import ce.repository.ReportsRepoImpl
import ce.repository.WrittersRepoImpl
import ce.settings.Project

class BuildProjectUseCase(
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeInTreeUseCase : StoreAstTreeUseCase,
    private val loadMetaFilesUseCase : LoadMetaFilesForTargetUseCase,
    private val storeOutTreeUseCase : StoreOutTreeUseCase,
    private val transformInTreeToOutTreeUseCase : TransformInTreeToOutTreeUseCase,
) {
    operator fun invoke(projectFile: String,
                        writeInTree: Boolean = false,
                        writeOutTree : Boolean = false,
                        dirsConfiguration: DirsConfiguration) {
        val project : Project = getProjectUseCase(projectFile, dirsConfiguration)
        val codeStyleRepo = CodestyleRepoImpl(project)
        val reportsRepo = ReportsRepoImpl()
        reportsRepo.logi("Processing $project")
        val generatorsRepo = GeneratorsRepo(
            project = project,
            codestylesRepo = codeStyleRepo)
        val writtersRepo = WrittersRepoImpl(codestylesRepo = codeStyleRepo,
            reportsRepo = reportsRepo
        )

        project.targets.forEach { target ->
            val inputTree = loadMetaFilesUseCase(project, target)
            if (writeInTree) {
                storeInTreeUseCase(target.outputFolder + "input_tree_${target.type.name}.xml", inputTree)
            }
            val outTree = transformInTreeToOutTreeUseCase(inputTree, generatorsRepo.get(target.type))
            if (writeOutTree) {
                storeOutTreeUseCase(target.outputFolder + "output_tree_${target.type.name}.xml", outTree)
            }
            val codeStyleTree = generatorsRepo.get(outTree.target).prepareCodeStyleTree(outTree)
            writtersRepo.getWritter(target).write(codeStyleTree)
        }
    }
}