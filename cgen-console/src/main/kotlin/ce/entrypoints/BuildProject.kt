package ce.entrypoints

import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.entry.PrepareInTreeUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase

fun main(args: Array<String>) {
    var needToStoreInTree = false
    var needToStoreOutTree = false
    var projectFile = ""

    val it = args.iterator()
    while (it.hasNext()) {
        when (it.next()) {
            "--project" -> projectFile = it.next()
            "--storeOutTree" -> needToStoreOutTree = true
            "--storeInTree" -> needToStoreInTree = true
        }
    }

    if (args.size < 2) {
        error("""
            Please specify arguments:
              --project project_file
              --storeOutTree store all out tree's
              --storeInTree store input tree (AST)
        """.trimIndent())
    }

    val buildProjectUseCase = BuildProjectUseCase(
        getProjectUseCase = LoadProjectUseCase(),
        storeInTreeUseCase = StoreInTreeUseCase(),
        loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(),
        storeOutTreeUseCase = StoreOutTreeUseCase(),
        transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
    )

    buildProjectUseCase(projectFile, needToStoreInTree, needToStoreOutTree)
}
