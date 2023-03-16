package ce.entrypoints

import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please, specify project file!")
    }

    val buildProjectUseCase = BuildProjectUseCase(
        getProjectUseCase = LoadProjectUseCase(),
        storeInTreeUseCase = StoreInTreeUseCase(),
        loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(),
        storeOutTreeUseCase = StoreOutTreeUseCase(),
        transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
    )
    buildProjectUseCase(args[0])
}
