package generators.kotlin

import ce.settings.CodeStyle
import generators.obj.FileGenerator
import generators.obj.out.FileData

class KotlinFileGenerator(style: CodeStyle) : FileGenerator(style) {
    override fun createFile(namespace: String, outputFile: String): FileData {
        return FileData().apply {
            this.namespace = namespace
            this.fullOutputFileName= outputFile
            appendHeaderLine("package $namespace\n")
        }
    }

    override fun newLine(): String = "\n"
    override fun multilineCommentStart(): String = "/**${newLine()}"
    override fun multilineCommentEnd(): String = "*/${newLine()}"
    override fun commentStart(): String = "/*${newLine()}"
    override fun commentEnd(): String = "*/${newLine()}"

    override fun appendInitalComment(s: FileData, s1: String) {
        if (s1.trimIndent().isNotEmpty()) {
            s1.lines().forEach { line ->
                s.initialComments.append("* $line${newLine()}")
            }
        }
    }
}