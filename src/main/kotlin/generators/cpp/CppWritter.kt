package generators.cpp

import ce.settings.CodeStyle
import generators.obj.Writter
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class CppWritter(codeStyle: CodeStyle, outputFolder: String) : Writter<CppClassData>(codeStyle, outputFolder) {

//    fun writeHeaderFile(data: CppHeaderData) {
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

//    override fun write(data: CppClassData) {
//        writeHeaderFile(data.headerData)
//        writeHeaderFile(data)
//    }

    override fun writeFile(fileData: FileData) {
        var customFolder = File(fileData.fullOutputFileName)
        customFolder.parentFile.mkdirs()
//        if (data.headers.isEmpty() && data.classDefinition.isEmpty() && data.end.isEmpty()) {
//            println("Writing $customFolder / $filename is empty")
//            return;
//        }
        println("Writing $customFolder")
    }

    override fun write(data: ProjectOutput) {
        data.files.forEach {
            writeFile(it.value)
        }
    }

    override fun getIncludes(data: FileData): StringBuilder = StringBuilder().apply {
        data.includes.forEach {
            this.append("#include $it\n")
        }
    }

}