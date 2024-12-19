package ce.domain.usecase.load

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.domain.DirsConfiguration
import ce.settings.Project
import ce.treeio.DataTypeSerializer
import ce.treeio.DataValueSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.FileInputStream

interface LoadProjectUseCase {
    operator fun invoke(projectPath: String, dirs: DirsConfiguration): Project
}

class LoadProjectUseCaseImpl : LoadProjectUseCase {
    override operator fun invoke(
        projectFileRelativePath: String,
        dirs: DirsConfiguration
    ): Project {
        val projectFilePath = if (projectFileRelativePath.startsWith("/"))
            projectFileRelativePath
        else
            "${dirs.workingDir}/$projectFileRelativePath"
        println("Loading project $projectFilePath")
        val mapper = ObjectMapper()
            .registerModule(
                KotlinModule.Builder()
                    .build()
            )
            .enable(SerializationFeature.INDENT_OUTPUT)
        val module = SimpleModule()
        module.addSerializer(DataType::class.java, DataTypeSerializer())
        module.addSerializer(DataValue::class.java, DataValueSerializer())
        mapper.registerModule(module)

        // load project file
        try {
            val projectJson = FileInputStream(projectFilePath)
            val project: Project = mapper.readValue(projectJson, Project::class.java)
            projectJson.close()
            return project.copy(dirsConfiguration = dirs)
        } catch (err: Exception) {
            System.err.println("Can't load project file $projectFileRelativePath")
            err.printStackTrace(System.err)
            throw err
        }
    }
}