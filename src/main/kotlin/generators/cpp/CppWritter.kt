package generators.cpp

import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class CppWritter(val fileGenerator: FileGenerator, outputFolder: String)
    : Writter(fileGenerator.style, outputFolder) {

    fun writeHeaderFile(fileData: CppFileData) {
        var outputFile = File(fileData.name + ".h")
        outputFile.parentFile.mkdirs()
        println("Writing headers $outputFile")
        outputFile.bufferedWriter().use { out ->
            writeNotEmpty(out, fileData.headerInitialComments)
            writeNotEmpty(out, fileData.headerBegin)

            fileData.namespaces.forEach { ns ->
                if (ns.key.isNotEmpty())
                 out.write("namespace ${ns.key} {${fileGenerator.newLine()}");

                ns.value.subs.forEach {
//                    val classDecl = (it.value as CppClassData).headerData
//                    writeNotEmpty(out, classDecl.classStart)
//
//                    for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
//
//                    if (classDecl.classComment.isNotEmpty()) {
//                        out.write(fileGenerator.multilineCommentStart())
//                        classDecl.classComment.lines().forEach { line ->
//                            out.write(fileGenerator.multilineCommentMid())
//                            out.write(" $line${fileGenerator.newLine()}")
//                        }
//                        out.write(fileGenerator.multilineCommentEnd())
//                    }
//
//                    if (classDecl.classDefinition.isNotEmpty()) {
//                        out.write(classDecl.classDefinition.toString())
//                    }
//
//                    writeNotEmpty(out, classDecl.classEnd)
                }

                if (ns.key.isNotEmpty())
                    out.write("} // ${ns.key}${fileGenerator.newLine()}")
            }

            if (fileData.end.isNotEmpty()) {
                out.write(fileData.end.toString())
            }
        }
    }

    override fun writeFile(fileData: FileData) {
        writeHeaderFile(fileData as CppFileData)
        var outputFile = File(fileData.name + ".cpp")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        outputFile.bufferedWriter().use { out ->
            writeNode(fileData, out)

            val headers = fileData.getHeaders()
            if (headers.isNotEmpty()) {
                out.write(headers)
            }

//            fileData.outputBlocks.forEach {
//                if (it.value.classDefinition.isNotEmpty()) {
//                    for (i in 0..codeStyle.newLinesBeforeClass - 1) out.write(fileGenerator.newLine())
//                    out.write(it.value.classDefinition.toString())
//                }
//            }

            if (fileData.end.isNotEmpty()) {
                out.write(fileData.end.toString())
            }
        }
    }

    override fun write(data: ProjectOutput) {
        data.subs.forEach {
            if (it is FileData) {
                writeFile(it)
            }
        }
    }
}