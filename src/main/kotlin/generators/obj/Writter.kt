package generators.obj

import ce.settings.CodeStyle
import generators.kotlin.KotlinClassData
import generators.obj.out.ClassData
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

abstract class Writter(val codeStyle: CodeStyle, outputFolderPath: String) {
    val outFolder : File

    init {
        outFolder = File(outputFolderPath)
        outFolder.mkdirs()
    }

    abstract fun write(data: ProjectOutput)
    abstract fun writeFile(fileData: FileData)
    abstract fun getIncludes(fileData: FileData) : StringBuilder
}