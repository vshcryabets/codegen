package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.Writter
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class KotlinWritter(val fileGenerator: KotlinFileGenerator, outputFolder: String)
    : Writter<KotlinClassData>(fileGenerator.style, outputFolder) {

    override fun write(data: ProjectOutput) {
        data.files.forEach {
            writeFile(it.value)
        }
    }

    override fun writeFile(fileData: FileData) {
        var outputFile = File(fileData.fullOutputFileName)
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            val initialComments = fileData.getInitialComments()
            if (initialComments.isNotEmpty()) {
                out.write(fileGenerator.multilineCommentStart())
                out.write(initialComments)
                out.write(fileGenerator.multilineCommentEnd())
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

    override fun getIncludes(data: FileData): StringBuilder = StringBuilder().apply {
        data.includes.forEach {
            this.append("import $it\n")
        }
    }
}