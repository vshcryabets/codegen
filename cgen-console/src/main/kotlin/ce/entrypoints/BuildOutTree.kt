package ce.entrypoints

import ce.defs.Target
import ce.defs.TargetExt
import ce.defs.domain.DirsConfiguration
import ce.domain.usecase.entry.BuildOutTreeUseCase
import ce.domain.usecase.entry.BuildOutTreeUseCaseImpl
import ce.domain.usecase.load.LoadAstTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import java.io.File

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

    val buildOutTreeUseCase = BuildOutTreeUseCaseImpl(
        LoadAstTreeUseCase(),
        LoadProjectUseCaseImpl(),
        StoreOutTreeUseCase(),
        TransformInTreeToOutTreeUseCase(),
    )
    val dir = DirsConfiguration(
        workingDir = File(".").absolutePath
    )
    buildOutTreeUseCase(
        projectFile = args[1],
        inTreeFile = args[0],
        outTreeFile = args[2],
        target = target,
        dirsConfiguration = dir
    )
}
