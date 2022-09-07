package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.Writter
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class KotlinWritter(codeStyle: CodeStyle, outputFolder: String) : Writter<KotlinClassData> (codeStyle, outputFolder) {

//    override fun write(data: KotlinClassData) {
//        File(customFolder, filename).bufferedWriter().use { out ->
//            if (data.headers.isNotEmpty()) {
//                out.write(data.headers.toString())
//            }
//
//            if (data.classDefinition.isNotEmpty()) {
//                for ( i in 0 .. codeStyle.newLinesBeforeClass - 1) out.write("\n")
//                out.write(data.classDefinition.toString())
//            }
//
//            if (data.end.isNotEmpty()) {
//                out.write(data.end.toString())
//            }
//        }
//    }

    override fun write(data: ProjectOutput) {
        data.files.forEach {
            writeFile(it.value)
        }
    }

    override fun writeFile(fileData: FileData) {
        val namespace = fileData.namespace.replace('.', File.separatorChar)
        val filename = namespace + File.separatorChar + fileData.fileName
        var customFolder = outFolder
        if (fileData.fileFolder.isNotEmpty()) {
            customFolder = File(fileData.fileFolder)
        }
        val outFolder = File(customFolder, namespace)
        outFolder.mkdirs()
        println("Writing $customFolder / $filename")
    }

    override fun getIncludes(data: FileData): StringBuilder = StringBuilder().apply {
        data.includes.forEach {
            this.append("import $it\n")
        }
    }
}