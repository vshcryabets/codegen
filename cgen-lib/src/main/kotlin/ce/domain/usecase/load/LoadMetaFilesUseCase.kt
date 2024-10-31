package ce.domain.usecase.load

import ce.defs.*
import ce.defs.Target
import ce.settings.Project
import generators.obj.input.Namespace
import generators.obj.input.clearSubs
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngine
import javax.script.ScriptException

class LoadMetaFilesForTargetUseCase constructor(
    private val groovyScriptEngine: ScriptEngine,
    private val kotlinScriptEngine: ScriptEngine,
) {

    operator fun invoke(project: Project, target : Target) : Namespace {
        println("Target $target")
        currentTarget = target
        globRootNamespace.clearSubs()

        project.files.forEach { fileName ->
            println("Processing $fileName")
            val fileObject = File(fileName)
            val reader = InputStreamReader(FileInputStream(fileObject))
            // clean global defines for each file
            customBaseFolderPath = project.outputFolder
            sourceFile = fileObject.absolutePath
            outputFile = ""
            try {
                if (fileName.endsWith(".kts")) {
                    kotlinScriptEngine.eval(reader)
                } else if (fileName.endsWith(".groovy")) {
                    groovyScriptEngine.eval(reader)
                } else {
                    System.err.println("Unknown file type $fileName")
                }
            }
            catch (error: ScriptException) {
                error("Error in file ${fileObject.absoluteFile} : ${error.message}")
                throw error
            }

            reader.close()
        }
        return globRootNamespace
    }
}