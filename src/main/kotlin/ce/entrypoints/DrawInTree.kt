package ce.entrypoints

import ce.domain.usecase.load.LoadInTreeUseCase
import ce.domain.usecase.store.StoreInTreeToSvgUseCaseImpl

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("Please, specify input tree file and output SVG!")
    }

    val loadInTreeUseCase = LoadInTreeUseCase()

    val tree = loadInTreeUseCase(args[0])
    val storeUseCase = StoreInTreeToSvgUseCaseImpl()
    storeUseCase(args[1], tree)
}
