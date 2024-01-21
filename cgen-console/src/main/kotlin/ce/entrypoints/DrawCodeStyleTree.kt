package ce.entrypoints

import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadCodeStyleTreeUseCase
import ce.domain.usecase.store.StoreTreeToSvgUseCase
import ce.domain.usecase.store.StoreTreeToSvgUseCaseImpl

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("Please, specify output tree file and output SVG!")
    }

    val loadInTreeUseCase = LoadCodeStyleTreeUseCase()

    val tree = loadInTreeUseCase(args[0])
    val storeUseCase = StoreTreeToSvgUseCaseImpl()
    storeUseCase(args[1], tree, StoreTreeToSvgUseCase.Location.Left)
}
