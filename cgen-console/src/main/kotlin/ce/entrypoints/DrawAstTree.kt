package ce.entrypoints

import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.store.StoreTreeToSvgUseCase
import ce.domain.usecase.store.StoreTreeToSvgUseCaseImpl

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("Please, specify input tree file and output SVG!")
    }

    val loadAstTreeUseCase = LoadAstTreeUseCase()

    val astTree = loadAstTreeUseCase(args[0])
    val storeUseCase = StoreTreeToSvgUseCaseImpl()
    val rootNamespace = astTree.subs.firstOrNull() ?: throw IllegalStateException("No root namespace")
    storeUseCase(args[1], rootNamespace, StoreTreeToSvgUseCase.Location.Left)
}
