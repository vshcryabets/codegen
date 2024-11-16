package ce.entrypoints

import ce.defs.domain.DirsConfiguration
import ce.domain.usecase.load.LoadOutTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.repository.CodestyleRepoImpl
import ce.repository.GeneratorsRepo
import ce.repository.WrittersRepoImpl
import java.io.File

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("""
            Please, specify: 
                - output tree file
                - project file
            """)
    }

    val loadOutputTreeUseCase = LoadOutTreeUseCase()
    val getProjectUseCase: LoadProjectUseCase = LoadProjectUseCaseImpl()
    val dir = DirsConfiguration(
        workingDir = File(".").absolutePath
    )
    val project = getProjectUseCase(args[1], dir)
    val codestylesRepo = CodestyleRepoImpl(project)
    val writtersFactoryImpl = WrittersRepoImpl(
        codestylesRepo = codestylesRepo)

    val generatorsRepo = GeneratorsRepo(project,
        codestylesRepo = codestylesRepo)
    val tree = loadOutputTreeUseCase(args[0])
    val codeStyleTree = generatorsRepo.get(tree.target).prepareCodeStyleTree(tree)
    val targetConfiguration = project.targets.firstOrNull { it.type == tree.target } ?:
        throw IllegalStateException("Can't find configuration for ${tree.target} in the project file ${args[1]}")
    writtersFactoryImpl.getWritter(targetConfiguration).write(codeStyleTree)
}
