package generators.kotlin

import ce.settings.CodeStyle
import java.io.File

class KotlinWritter(val codeStyle: CodeStyle, outputFolder: String) {
    val outFolderFile : File

    init {
        outFolderFile = File(outputFolder)
        outFolderFile.mkdirs()
    }

    fun write(data: ClassData) {
        println("Writting ${data.fileName}")
        File(outFolderFile, data.fileName).bufferedWriter().use { out ->
            if (data.headers.isNotEmpty()) {
                out.write(data.headers.toString())
            }

            if (data.classDefinition.isNotEmpty()) {
                for ( i in 0 .. codeStyle.newLinesBeforeClass - 1) out.write("\n")
                out.write(data.classDefinition.toString())
            }

            if (data.end.isNotEmpty()) {
                out.write(data.end.toString())
            }
        }
    }
}