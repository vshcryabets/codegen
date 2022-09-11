package generators.obj

import ce.settings.CodeStyle
import generators.obj.out.FileData

abstract class FileGenerator(val style : CodeStyle) {
    val tabSpace : String

    init {
        val builder = StringBuilder()
        for (a in 0..style.tabSize - 1) {
            builder.append(" ");
        }
        tabSpace = builder.toString()
    }

    abstract fun createFile(outputFile: String): FileData
    abstract fun multilineCommentStart() : String
    abstract fun multilineCommentEnd() : String
    abstract fun singleComment() : String
    abstract fun newLine() : String

    open fun appendInitalComment(s: FileData, s1: String) {
        if (s1.trimIndent().isNotEmpty()) {
            s1.lines().forEach { line ->
                s.initialComments.append("${singleComment()} $line${newLine()}")
            }
        }
    }

}