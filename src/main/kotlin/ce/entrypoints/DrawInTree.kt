package ce.entrypoints

import ce.domain.usecase.GetProjectUseCase
import ce.domain.usecase.load.LoadInTreeUseCase
import ce.settings.Project
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {
    if (args.size < 1) {
        error("Please, specify project file!")
    }

    val engine = ScriptEngineManager().getEngineByExtension("kts")

    val getProjectUseCase = GetProjectUseCase()
    val loadInTreeUseCase = LoadInTreeUseCase()

    val tree = loadInTreeUseCase(args[0])
    println(tree)
}
