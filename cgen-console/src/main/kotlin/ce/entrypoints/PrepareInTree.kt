package ce.entrypoints

import ce.domain.usecase.entry.PrepareInTreeUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please specify project file!")
    }
    val prepareInTreeUseCase = PrepareInTreeUseCase(
        LoadProjectUseCase(),
        StoreInTreeUseCase(),
        LoadMetaFilesForTargetUseCase()
    )
    prepareInTreeUseCase(args[0])
}
