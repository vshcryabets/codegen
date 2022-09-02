package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.Writter
import java.io.File

class KotlinWritter(codeStyle: CodeStyle, outputFolder: String) : Writter(codeStyle, outputFolder) {

    override fun write(data: ClassData) {
        val namespace = data.namespace.replace('.', File.separatorChar)
        val filename = namespace + File.separatorChar + data.fileName
        println("Writing $filename")
        val outFolder = File(outFolderFile, namespace)
        outFolder.mkdirs()
        File(outFolderFile, filename).bufferedWriter().use { out ->
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