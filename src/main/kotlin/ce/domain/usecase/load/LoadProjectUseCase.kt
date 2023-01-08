package ce.domain.usecase.load

import ce.defs.DataType
import ce.defs.DataValue
import ce.settings.Project
import ce.treeio.DataTypeSerializer
import ce.treeio.DataValueSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import java.io.FileInputStream

class LoadProjectUseCase {
    operator fun invoke(projectPath : String) : Project {
        val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        val module = SimpleModule()
        module.addSerializer(DataType::class.java, DataTypeSerializer())
        module.addSerializer(DataValue::class.java, DataValueSerializer())
        mapper.registerModule(module)

        // load project file
        val projectJson = FileInputStream(projectPath)
        val project : Project = mapper.readValue(projectJson, Project::class.java)
        projectJson.close()
        return project
    }
}