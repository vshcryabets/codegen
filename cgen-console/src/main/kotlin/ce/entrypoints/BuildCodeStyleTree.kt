package ce.entrypoints

import ce.defs.Target
import ce.defs.TargetExt
import ce.domain.usecase.entry.BuildOutTreeUseCase
import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase

fun main(args: Array<String>) {
    if (args.size < 4) {
        error("""
            Please, specify next arguments: 
             - input tree file
             - project file
             - output tree file
            """ + " - target ${Target.values().filter { it != Target.Other }.map { it.toString() }}\n"
        )
    }

    val target = TargetExt.findByName(args[3])
    if (target == Target.Other) {
        error("Unknown target \"${args[3]}\"")
    }

    val buildOutTreeUseCase = BuildOutTreeUseCase(
        LoadAstTreeUseCase(),
        LoadProjectUseCase(),
        StoreOutTreeUseCase(),
        TransformInTreeToOutTreeUseCase(),
    )

    buildOutTreeUseCase(
        projectFile = args[1],
        inTreeFile = args[0],
        outTreeFile = args[2],
        target = target
    )
}
