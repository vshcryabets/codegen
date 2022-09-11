package generators.obj

import ce.settings.CodeStyle
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.BufferedWriter
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

    fun writeNotEmpty(out: BufferedWriter, builder: StringBuilder) {
        if (builder.isNotEmpty()) {
            out.write(builder.toString())
        }
    }
}