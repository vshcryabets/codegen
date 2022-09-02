package generators.obj

import ce.settings.CodeStyle
import generators.kotlin.ClassData
import java.io.File

abstract class Writter(val codeStyle: CodeStyle, outputFolder: String) {
    val outFolderFile : File

    init {
        outFolderFile = File(outputFolder)
        outFolderFile.mkdirs()
    }

    abstract fun write(data: ClassData)

}