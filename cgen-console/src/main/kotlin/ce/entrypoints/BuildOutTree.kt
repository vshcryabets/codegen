package ce.entrypoints

import ce.defs.Target
import ce.domain.usecase.entry.BuildOutTreeUseCase
import ce.domain.usecase.load.LoadInTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.GeneratorsRepo
import generators.obj.input.Node

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

    val target = Target.findByName(args[3])
    if (target == Target.Other) {
        error("Unknown target \"${args[3]}\"")
    }

    val buildOutTreeUseCase = BuildOutTreeUseCase(
        LoadInTreeUseCase(),
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
