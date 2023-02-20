package ce.entrypoints

import ce.defs.*
import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import ce.repository.GeneratorsRepo
import ce.settings.Project
import generators.obj.input.Node
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager
import javax.script.ScriptException

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please, specify project file!")
    }

    val buildProjectUseCase = BuildProjectUseCase(
        getProjectUseCase = LoadProjectUseCase(),
        storeInTreeUseCase = StoreInTreeUseCase(),
        loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(),
        storeOutTreeUseCase = StoreOutTreeUseCase(),
        transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
    )
    buildProjectUseCase(args[0])
}
