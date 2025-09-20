package ce.parser.domain.usecase.save

import ce.parser.domain.FormatProject
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.kotlin.javax.inject.Inject
import java.io.File

interface StoreFormatProject {
    fun store(project: FormatProject, file: File)
}

class StoreFormatProjectToJsonImpl @Inject constructor(
    private val objectMapper: ObjectMapper,
): StoreFormatProject {
    override fun store(project: FormatProject, file: File) {
        objectMapper.writeValue(file, project)
    }
}