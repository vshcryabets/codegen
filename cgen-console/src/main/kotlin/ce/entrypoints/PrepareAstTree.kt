package ce.entrypoints

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
    val kotlinScriptEngine = KotlinJsr223DefaultScriptEngineFactory().getScriptEngine()
    val factory = ScriptEngineManager()
    val groovyEngine = factory.getEngineByName("groovy")

    val prepareAstTreeUseCase = PrepareAstTreeUseCase(
        LoadProjectUseCaseImpl(),
        StoreAstTreeUseCase(),
        LoadMetaFilesForTargetUseCase(
            kotlinScriptEngine = kotlinScriptEngine,
            groovyScriptEngine = groovyEngine
        )
    )
    prepareAstTreeUseCase(args[0])
}
