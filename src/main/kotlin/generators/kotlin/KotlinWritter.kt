package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.Writter
import java.io.File

class KotlinWritter(codeStyle: CodeStyle, outputFolder: String) : Writter<KotlinClassData> (codeStyle, outputFolder) {

    override fun write(data: KotlinClassData) {
        val namespace = data.namespace.replace('.', File.separatorChar)
        val filename = namespace + File.separatorChar + data.fileName
        var customFolder = outFolder
        if (data.customBaseFolder.isNotEmpty()) {
            customFolder = File(data.customBaseFolder)
        }
        val outFolder = File(customFolder, namespace)
        outFolder.mkdirs()
        println("Writing $customFolder / $filename")
        File(customFolder, filename).bufferedWriter().use { out ->
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