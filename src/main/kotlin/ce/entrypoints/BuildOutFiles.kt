package ce.entrypoints

import ce.domain.usecase.load.LoadInTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.repository.GeneratorsRepo

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("Please, specify output tree file and output SVG!")
    }

    val loadInTreeUseCase = LoadInTreeUseCase()
    val tree = loadInTreeUseCase(args[0])
    val getProjectUseCase = LoadProjectUseCase()
    val generatorsRepo = GeneratorsRepo(getProjectUseCase(args[1]))



}
