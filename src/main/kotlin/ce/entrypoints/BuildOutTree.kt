package ce.entrypoints

import ce.defs.Target
import ce.domain.usecase.load.LoadInTreeUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import generators.obj.input.Node

fun main(args: Array<String>) {
    if (args.size < 2) {
        error("Please, specify input tree file, target and output tree file.")
    }

    val loadInTreeUseCase = LoadInTreeUseCase()
    val getProjectUseCase = LoadProjectUseCase()
    val transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase()
    val storeOutTreeUseCase = StoreOutTreeUseCase()

    val tree = loadInTreeUseCase(args[0])
    val outTree = transformInTreeToOutTreeUseCase(tree as Node, Target.Kotlin, getProjectUseCase(args[1]))
    storeOutTreeUseCase("outtree_kotlin.xml", outTree)
}
