package ce.entrypoints

import ce.defs.*
import ce.domain.usecase.GetProjectUseCase
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

    val engine = ScriptEngineManager().getEngineByExtension("kts")

    val getProjectUseCase = GetProjectUseCase()
    val storeInTreeUseCase = StoreInTreeUseCase()

    val project : Project = getProjectUseCase(args[0])
    println("Processing $project")

    project.targets.forEach { target ->
        println("Target $target")
        namespaceMap.clear()
        currentTarget = target
        globRootNamespace.subs.clear()

        project.files.forEach { fileName ->
            println("Processing $fileName")
            val fileObject = File(fileName)
            val reader = InputStreamReader(FileInputStream(fileObject))
            // clean global defines for each file
            globCurrentNamespace = globRootNamespace
            customBaseFolderPath = project.outputFolder
            sourceFile = fileObject.absolutePath
            outputFile = ""
            try {
                engine.eval(reader)
            }
            catch (error: ScriptException) {
                println("Error in file ${fileObject.absoluteFile}:${error.message}")
                System.exit(0)
            }

            reader.close()
        }

        storeInTreeUseCase(project.outputFolder + "input_tree_${target.name}.xml", globRootNamespace)
    }
}
