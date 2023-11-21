package ce.entrypoints

import ce.domain.usecase.load.LoadOutTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.repository.GeneratorsRepo

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("""
            Please, specify: 
                - output tree file
                - project file
            """)
    }

    val loadOutputTreeUseCase = LoadOutTreeUseCase()
    val getProjectUseCase = LoadProjectUseCase()

    val project = getProjectUseCase(args[1])
    val generatorsRepo = GeneratorsRepo(project)
    val tree = loadOutputTreeUseCase(args[0])
    val codeStyleTree = generatorsRepo.get(tree.target).prepareCodeStyleTree(tree)
    generatorsRepo.get(tree.target).write(codeStyleTree)
}
