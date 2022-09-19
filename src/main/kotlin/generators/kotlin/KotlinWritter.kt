package generators.kotlin

import generators.obj.Writter
import generators.obj.out.*
import java.io.BufferedWriter
import java.io.File

class KotlinWritter(val fileGenerator: KotlinFileGenerator, outputFolder: String)
    : Writter(fileGenerator.style, outputFolder) {

    override fun write(data: ProjectOutput) {
        data.files.forEach {
            writeFile(it.value)
        }
    }

    override fun writeFile(fileData: FileData) {
        if (fileData.namespaces.size != 1) {
            throw IllegalStateException("Kotlin file can contain only one namespace")
        }
        var outputFile = File(fileData.fullOutputFileName + ".kt")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        val namespace = fileData.namespaces.entries.first().value
        // change file namespace according to the 1st block
        fileData.findSub(NamespaceDeclaration::class.java).name = namespace.name
        outputFile.bufferedWriter().use { out ->
            writeNode(fileData, out)

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

    override fun writeLeaf(leaf: OutLeaf, out: BufferedWriter) {
        when (leaf) {
            is ImportLeaf -> out.write("import ${leaf.line}${fileGenerator.newLine()}")
            is NamespaceDeclaration -> out.write("package ${leaf.name}${fileGenerator.newLine()}")
            else -> super.writeLeaf(leaf, out)
        }
    }
}