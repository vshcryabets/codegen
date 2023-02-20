package ce.domain.usecase.entry

import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.settings.Project

class PrepareInTreeUseCase(
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeInTreeUseCase : StoreInTreeUseCase,
    private val loadMetaFilesUseCase : LoadMetaFilesForTargetUseCase,
) {
    operator fun invoke(projectFile: String) {
        val project : Project = getProjectUseCase(projectFile)
        println("Processing $project")

        project.targets.forEach { target ->
            val root = loadMetaFilesUseCase(project, target)
            storeInTreeUseCase(project.outputFolder + "input_tree_${target.name}.xml", root)
        }
    }
}