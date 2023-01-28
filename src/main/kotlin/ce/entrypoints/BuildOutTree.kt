package ce.entrypoints

import ce.defs.Target
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

    val loadInTreeUseCase = LoadInTreeUseCase()
    val getProjectUseCase = LoadProjectUseCase()
    val storeOutTreeUseCase = StoreOutTreeUseCase()
    val transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase()

    val generatorsRepo = GeneratorsRepo(getProjectUseCase(args[1]))
    val target = Target.findByName(args[3])
    if (target == Target.Other) {
        error("Unknown target \"${args[3]}\"")
    }

    val tree = loadInTreeUseCase(args[0])
    val outTree = transformInTreeToOutTreeUseCase(tree as Node, generatorsRepo.get(target))
    storeOutTreeUseCase(args[2], outTree)
}
