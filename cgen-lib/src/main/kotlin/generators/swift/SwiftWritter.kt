package generators.swift

import ce.formatters.CodeStyleRepo
import ce.io.FileCodeWritter
import ce.repository.ReportsRepo
import generators.obj.Writter
import generators.obj.syntaxParseTree.FileData
import java.io.File

class SwiftWritter(
    fileGenerator: SwiftFileGenerator, codeStyleRepo: CodeStyleRepo,
    outputFolder: String,
    private val reportsRepo: ReportsRepo
) : Writter(codeStyleRepo, outputFolder) {

    override fun writeFile(fileData: FileData) {
//        if (fileData.namespaces.size != 1) {
//            throw IllegalStateException("Swift file can contain only one namespace")
//        }
        var outputFile = File(fileData.name + ".swift")
        outputFile.parentFile.mkdirs()
        reportsRepo.logi("Writing $outputFile")
//        val namespace = fileData.namespaces.entries.first().value
        outputFile.bufferedWriter().use { out ->
            val codeWritter = FileCodeWritter(out)
            codeWritter.setNewLine(codeStyleRepo.newLine())
            writeNode(fileData, codeWritter, "")

            //out.write("package ${namespace.name}${fileGenerator.newLine()}");

//            val headers = fileData.getHeaders()
//            if (headers.isNotEmpty()) {
//                out.write(headers)
//            }
//
//            namespace.subs.forEach {
//                val classDef = it.value
//                writeNotEmpty(out, classDef.classStart)
//
//                for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
//
//                if (classDef.classComment.isNotEmpty()) {
//                    out.write(fileGenerator.multilineCommentStart())
//                    classDef.classComment.lines().forEach { line ->
//                        out.write(fileGenerator.multilineCommentMid())
//                        out.write(" $line${fileGenerator.newLine()}")
//                    }
//                    out.write(fileGenerator.multilineCommentEnd())
//                }
//
//                if (classDef.classDefinition.isNotEmpty()) {
//                    out.write(classDef.classDefinition.toString())
//                }
//
//                writeNotEmpty(out, classDef.classEnd)
//            }
        }
    }
}