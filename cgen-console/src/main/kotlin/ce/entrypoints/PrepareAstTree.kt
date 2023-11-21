package ce.entrypoints

import ce.domain.usecase.entry.PrepareAstTreeUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreAstTreeUseCase

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please specify project file!")
    }
    val prepareAstTreeUseCase = PrepareAstTreeUseCase(
        LoadProjectUseCase(),
        StoreAstTreeUseCase(),
        LoadMetaFilesForTargetUseCase()
    )
    prepareAstTreeUseCase(args[0])
}
