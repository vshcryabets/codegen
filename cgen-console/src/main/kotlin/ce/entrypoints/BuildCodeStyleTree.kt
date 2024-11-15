package ce.entrypoints

import ce.defs.domain.DirsConfiguration
import ce.domain.usecase.entry.BuildCodeStyleTreeUseCase
import ce.domain.usecase.load.LoadOutTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.domain.usecase.store.StoreCodeStyleTreeUseCase
import ce.domain.usecase.transform.TransformOutTreeToCodeStyleTreeUseCase
import java.io.File

fun main(args: Array<String>) {
    if (args.size < 3) {
        error("""
            Please, specify next arguments: 
             - output tree file
             - project file
             - codes styled output tree file
            """
        )
    }

    val buildCodeStyleOutTreeUseCase = BuildCodeStyleTreeUseCase(
        LoadOutTreeUseCase(),
        LoadProjectUseCaseImpl(),
        StoreCodeStyleTreeUseCase(),
        TransformOutTreeToCodeStyleTreeUseCase(),
    )
    val dir = DirsConfiguration(
        workingDir = File(".").absolutePath
    )

    buildCodeStyleOutTreeUseCase(
        outTreeFile = args[0],
        projectFile = args[1],
        codeStyleTreeFile = args[2],
        dirsConfiguration = dir
    )
}
