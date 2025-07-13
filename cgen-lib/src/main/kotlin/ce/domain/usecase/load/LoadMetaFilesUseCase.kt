package ce.domain.usecase.load

import ce.defs.MetaEngine
import ce.defs.TargetConfiguration
import ce.defs.currentTarget
import ce.defs.customBaseFolderPath
import ce.defs.globRootNamespace
import ce.defs.outputFile
import ce.defs.sourceFile
import ce.settings.Project
import ce.treeio.TreeFunctions
import generators.obj.input.Namespace
import generators.obj.input.Node
import generators.obj.input.clearSubs
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.script.ScriptEngine
import javax.script.ScriptException

class LoadMetaFilesForTargetUseCase @Inject constructor(
    private val enginesMap: Map<MetaEngine, ScriptEngine>,
    private val loadXmlTreeUseCase: LoadXmlTreeUseCase
) {

    operator fun invoke(project: Project, target : TargetConfiguration) : Namespace {
        println("Target ${target.type}")
        currentTarget = target.type
        globRootNamespace.clearSubs()

        project.files.forEach { fileName ->
            val fileObject = File(project.dirsConfiguration.workingDir + File.separator + fileName)
            println("Processing ${fileObject.absolutePath} out folder ${target.outputFolder}")
            val reader = InputStreamReader(FileInputStream(fileObject))
            // clean global defines for each file
            customBaseFolderPath = target.outputFolder
            sourceFile = fileObject.absolutePath
            outputFile = ""
            try {
                if (fileName.endsWith(".kts")) {
                    enginesMap[MetaEngine.KTS]?.eval(reader) ?: println("KTS engine is null, can't process $fileName")
                } else if (fileName.endsWith(".groovy")) {
                    enginesMap[MetaEngine.GROOVY]?.eval(reader) ?: println("Groovy engine is null, can't process $fileName")
                } else if (fileName.endsWith(".xml")) {
                    val xmlTree = loadXmlTreeUseCase.fromFile(fileName)
                    if (xmlTree !is Node) {
                        println("$fileName doesn't contain Node description")
                    } else {
                        if (xmlTree.name.isNotEmpty()) {
                            val subSpace = globRootNamespace.getNamespace(xmlTree.name)
                            TreeFunctions.mergeTrees(subSpace, xmlTree)
                        } else {
                            TreeFunctions.mergeTrees(globRootNamespace, xmlTree)
                        }
                    }
                } else {
                    error("Unknown file type $fileName")
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