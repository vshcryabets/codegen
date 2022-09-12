package generators.swift

import generators.obj.Writter
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class SwiftWritter(val fileGenerator: SwiftFileGenerator, outputFolder: String)
    : Writter(fileGenerator.style, outputFolder) {

    override fun write(data: ProjectOutput) {
        data.files.forEach {
            writeFile(it.value)
        }
    }

    override fun writeFile(fileData: FileData) {
        if (fileData.namespaces.size != 1) {
            throw IllegalStateException("Swift file can contain only one namespace")
        }
        var outputFile = File(fileData.fullOutputFileName + ".swift")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        val namespace = fileData.namespaces.entries.first().value
        outputFile.bufferedWriter().use { out ->
            writeNotEmpty(out, fileData.initialComments)

            //out.write("package ${namespace.name}${fileGenerator.newLine()}");

            val headers = fileData.getHeaders()
            if (headers.isNotEmpty()) {
                out.write(headers)
            }

            namespace.outputBlocks.forEach {
                val classDef = it.value
                writeNotEmpty(out, classDef.classStart)

                for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())

                if (classDef.classComment.isNotEmpty()) {
                    out.write(fileGenerator.multilineCommentStart())
                    classDef.classComment.lines().forEach { line ->
                        out.write(fileGenerator.multilineCommentMid())
                        out.write(" $line${fileGenerator.newLine()}")
                    }
                    out.write(fileGenerator.multilineCommentEnd())
                }

                if (classDef.classDefinition.isNotEmpty()) {
                    out.write(classDef.classDefinition.toString())
                }

                writeNotEmpty(out, classDef.classEnd)
            }

            if (fileData.end.isNotEmpty()) {
                out.write(fileData.end.toString())
            }
        }
    }

    override fun getIncludes(data: FileData): StringBuilder = StringBuilder().apply {
        data.includes.forEach {
            this.append("import $it${fileGenerator.newLine()}")
        }
    }
}