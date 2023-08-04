package ce.domain.usecase.load

import ce.defs.*
import ce.defs.Target
import ce.settings.Project
import generators.obj.input.Namespace
import generators.obj.input.clearSubs
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptException
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory

class LoadMetaFilesForTargetUseCase {

    operator fun invoke(project: Project, target : Target) : Namespace {
        //val engine = ScriptEngineManager().getEngineByExtension("kts")
        val engine = KotlinJsr223DefaultScriptEngineFactory().getScriptEngine()

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
                engine.eval(reader)
            }
            catch (error: ScriptException) {
                error("Error in file ${fileObject.absoluteFile}:${error.message}")
                System.exit(0)
            }

            reader.close()
        }
        return globRootNamespace
    }
}