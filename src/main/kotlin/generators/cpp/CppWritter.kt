package generators.cpp

import ce.settings.CodeStyle
import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class CppWritter(val fileGenerator: FileGenerator, outputFolder: String)
    : Writter(fileGenerator.style, outputFolder) {

    fun writeHeaderFile(fileData: FileData) {
        var outputFile = File(fileData.fullOutputFileName + ".h")
        outputFile.parentFile.mkdirs()
        println("Writing headers $outputFile")
        outputFile.bufferedWriter().use { out ->
            val initialComments = fileData.getInitialComments()
            if (initialComments.isNotEmpty()) {
                out.write(initialComments)
            }

            val headers = fileData.getHeaders()
            if (headers.isNotEmpty()) {
                out.write(headers)
            }

            fileData.outputBlocks.forEach {
                if (it.value.classDefinition.isNotEmpty()) {
                    for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
                    out.write(it.value.classDefinition.toString())
                }
            }

            if (fileData.end.isNotEmpty()) {
                out.write(fileData.end.toString())
            }
        }
    }

    override fun writeFile(fileData: FileData) {
        val cppFileData= fileData as CppFileData
        writeHeaderFile(cppFileData.headerFile)
        var outputFile = File(fileData.fullOutputFileName + ".cpp")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val initialComments = fileData.getInitialComments()
            if (initialComments.isNotEmpty()) {
                out.write(initialComments)
            }

            val headers = fileData.getHeaders()
            if (headers.isNotEmpty()) {
                out.write(headers)
            }

            fileData.outputBlocks.forEach {
                if (it.value.classDefinition.isNotEmpty()) {
                    for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
                    out.write(it.value.classDefinition.toString())
                }
            }

            if (fileData.end.isNotEmpty()) {
                out.write(fileData.end.toString())
            }
        }
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