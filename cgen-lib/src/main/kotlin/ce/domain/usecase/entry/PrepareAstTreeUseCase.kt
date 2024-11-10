package ce.domain.usecase.entry

import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreAstTreeUseCase
import ce.settings.Project
import generators.obj.out.AstTree

class PrepareAstTreeUseCase(
    private val getProjectUseCase : LoadProjectUseCase,
    private val storeInTreeUseCase : StoreAstTreeUseCase,
    private val loadMetaFilesUseCase : LoadMetaFilesForTargetUseCase,
) {
    operator fun invoke(projectFile: String) {
        val project : Project = getProjectUseCase(projectFile)
        println("Processing $project")

        project.targets.forEach { target ->
            val rootNameSpace = loadMetaFilesUseCase(project, target)
            val astTree = AstTree(
                target = target.type,
                subs = mutableListOf(rootNameSpace)
            )
            storeInTreeUseCase(target.outputFolder + "ast_tree_${target.type.name}.xml", astTree)
        }
    }
}