package ce.entrypoints

import ce.defs.MetaEngine
import ce.domain.usecase.entry.BuildProjectUseCase
import ce.domain.usecase.load.LoadMetaFilesForTargetUseCase
import ce.domain.usecase.load.LoadProjectUseCase
import ce.domain.usecase.load.LoadProjectUseCaseImpl
import ce.domain.usecase.store.StoreAstTreeUseCase
import ce.domain.usecase.store.StoreOutTreeUseCase
import ce.domain.usecase.transform.TransformInTreeToOutTreeUseCase
import javax.script.ScriptEngineManager
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory

fun main(args: Array<String>) {
    var needToStoreInTree = false
    var needToStoreOutTree = false
    var projectFile = ""

    val it = args.iterator()
    while (it.hasNext()) {
        when (it.next()) {
            "--project" -> projectFile = it.next()
            "--storeOutTree" -> needToStoreOutTree = true
            "--storeInTree" -> needToStoreInTree = true
        }
    }

    if (args.size < 2) {
        error("""
            Please specify arguments:
              --project project_file
              --storeOutTree store all out tree's
              --storeInTree store input tree (AST)
        """.trimIndent())
    }

    val engineMaps = mapOf(
        MetaEngine.KTS to KotlinJsr223DefaultScriptEngineFactory().getScriptEngine(),
        MetaEngine.GROOVY to ScriptEngineManager().getEngineByName("groovy")
    )

    val buildProjectUseCase = BuildProjectUseCase(
        getProjectUseCase = LoadProjectUseCaseImpl(),
        storeInTreeUseCase = StoreAstTreeUseCase(),
        loadMetaFilesUseCase = LoadMetaFilesForTargetUseCase(
            enginesMap =  engineMaps
        ),
        storeOutTreeUseCase = StoreOutTreeUseCase(),
        transformInTreeToOutTreeUseCase = TransformInTreeToOutTreeUseCase(),
    )

    buildProjectUseCase(projectFile, needToStoreInTree, needToStoreOutTree)
}
