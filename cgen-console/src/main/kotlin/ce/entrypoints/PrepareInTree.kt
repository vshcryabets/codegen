package ce.entrypoints

import ce.defs.*
import ce.domain.usecase.entry.PrepareInTreeUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.store.StoreInTreeUseCase
import ce.settings.Project
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager
import javax.script.ScriptException

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please, specify project file!")
    }
    val prepareInTreeUseCase = PrepareInTreeUseCase(
        LoadProjectUseCase(),
        StoreInTreeUseCase(),
        LoadMetaFilesForTargetUseCase()
    )
    prepareInTreeUseCase(args[0])
}
