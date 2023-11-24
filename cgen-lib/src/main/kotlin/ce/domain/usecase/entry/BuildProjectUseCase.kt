package ce.domain.usecase.entry

import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreAstTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.GeneratorsRepo
import ce.settings.Project

class BuildProjectUseCase(
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeInTreeUseCase : StoreAstTreeUseCase,
    private val loadMetaFilesUseCase : LoadMetaFilesForTargetUseCase,
    private val storeOutTreeUseCase : StoreOutTreeUseCase,
    private val transformInTreeToOutTreeUseCase : TransformInTreeToOutTreeUseCase,
) {
    operator fun invoke(projectFile: String, writeInTree: Boolean = false, writeOutTree : Boolean = false) {
        val project : Project = getProjectUseCase(projectFile)
        println("Processing $project")
        val generatorsRepo = GeneratorsRepo(project)

        project.targets.forEach { target ->
            val inputTree = loadMetaFilesUseCase(project, target)
            if (writeInTree) {
                storeInTreeUseCase(project.outputFolder + "input_tree_${target.name}.xml", inputTree)
            }
            val outTree = transformInTreeToOutTreeUseCase(inputTree, generatorsRepo.get(target))
            if (writeOutTree) {
                storeOutTreeUseCase(project.outputFolder + "output_tree_${target.name}.xml", outTree)
            }
            val codeStyleTree = generatorsRepo.get(outTree.target).prepareCodeStyleTree(outTree)
            generatorsRepo.getWritter(outTree.target).write(codeStyleTree)
        }
    }
}