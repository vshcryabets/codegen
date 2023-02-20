package ce.entrypoints

import ce.domain.usecase.load.LoadInTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.repository.GeneratorsRepo
import generators.obj.out.ProjectOutput

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("""
            Please, specify: 
                - output tree file
                - project file
            """)
    }

    val loadInTreeUseCase = LoadInTreeUseCase()
    val getProjectUseCase = LoadProjectUseCase()

    val project = getProjectUseCase(args[1])
    val generatorsRepo = GeneratorsRepo(project)
    val tree = loadInTreeUseCase(args[0]) as ProjectOutput

    generatorsRepo.get(tree.target).write(tree)
}
