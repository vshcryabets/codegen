package ce.entrypoints

import ce.defs.MetaEngine
import ce.domain.usecase.entry.PrepareAstTreeUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.domain.usecase.store.StoreAstTreeUseCase
import javax.script.ScriptEngineManager
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please specify project file!")
    }
    val engineMaps = mapOf(
        MetaEngine.KTS to KotlinJsr223DefaultScriptEngineFactory().getScriptEngine(),
        MetaEngine.GROOVY to ScriptEngineManager().getEngineByName("groovy")
    )

    val prepareAstTreeUseCase = PrepareAstTreeUseCase(
        LoadProjectUseCaseImpl(),
        StoreAstTreeUseCase(),
        LoadMetaFilesForTargetUseCase(engineMaps)
    )
    prepareAstTreeUseCase(args[0])
}
