package ce.domain.usecase.entry

import ce.defs.Target
import ce.domain.usecase.load.LoadInTreeUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.GeneratorsRepo
import ce.settings.Project
import generators.obj.input.Node

class BuildProjectUseCase(
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeInTreeUseCase : StoreInTreeUseCase,
    private val loadMetaFilesUseCase : LoadMetaFilesForTargetUseCase,
    private val storeOutTreeUseCase : StoreOutTreeUseCase,
    private val transformInTreeToOutTreeUseCase : TransformInTreeToOutTreeUseCase,
) {
    operator fun invoke(projectFile: String) {
        val project : Project = getProjectUseCase(projectFile)
        println("Processing $project")
        val generatorsRepo = GeneratorsRepo(project)

        project.targets.forEach { target ->
            val root = loadMetaFilesUseCase(project, target)
            storeInTreeUseCase(project.outputFolder + "input_tree_${target.name}.xml", root)
            val outTree = transformInTreeToOutTreeUseCase(root, generatorsRepo.get(target))
            storeOutTreeUseCase(project.outputFolder + "output_tree_${target.name}.xml", outTree)
        }
    }
}