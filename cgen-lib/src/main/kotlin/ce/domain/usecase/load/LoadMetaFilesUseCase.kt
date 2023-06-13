package ce.domain.usecase.load

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.Target
import ce.defs.currentTarget
import ce.defs.customBaseFolderPath
import ce.defs.globCurrentNamespace
import ce.defs.globRootNamespace
import ce.defs.namespaceMap
import ce.defs.outputFile
import ce.defs.sourceFile
import ce.settings.Project
import ce.treeio.DataTypeSerializer
import ce.treeio.DataValueSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import generators.obj.input.Namespace
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class LoadMetaFilesForTargetUseCase {

    operator fun invoke(project: Project, target : Target) : Namespace {
        val engine = ScriptEngineManager().getEngineByExtension("kts")

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
                error("Error in file ${fileObject.absoluteFile}:${error.message}")
                System.exit(0)
            }

            reader.close()
        }
        return globRootNamespace
    }
}