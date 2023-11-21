package ce.entrypoints

import ce.defs.Target
import ce.defs.TargetExt
import ce.domain.usecase.entry.BuildCodeStyleTreeUseCase
import ce.domain.usecase.entry.BuildOutTreeUseCase
import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadOutTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreCodeStyleTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.domain.usecase.transform.TransformOutTreeToCodeStyleTreeUseCase

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
        LoadProjectUseCase(),
        StoreCodeStyleTreeUseCase(),
        TransformOutTreeToCodeStyleTreeUseCase(),
    )

    buildCodeStyleOutTreeUseCase(
        outTreeFile = args[0],
        projectFile = args[1],
        codeStyleTreeFile = args[2]
    )
}
