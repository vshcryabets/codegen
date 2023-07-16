package ce.io

import java.io.BufferedWriter

class FileCodeWritter(
    private val out: BufferedWriter
) : CodeWritter {
    var indentStr = ""
    var newLineStr = "\n"

    var isNewEmptyLine = true

    override fun write(str: String): FileCodeWritter {
        if (isNewEmptyLine) {
            isNewEmptyLine = false
            out.write(indentStr)
        }
        out.write(str)
        return this
    }

    override fun writeNl(): CodeWritter {
        out.write(newLineStr)
        isNewEmptyLine = true
        return this
    }

    override fun writeNlIfNotEmpty(): FileCodeWritter {
        if (!isNewEmptyLine) {
            out.write(newLineStr)
            isNewEmptyLine = true
        }
        return this
    }

    override fun setIndent(str: String): FileCodeWritter {
        indentStr = str
        return this
    }

    override fun setNewLine(str: String) {
        newLineStr = str
    }
}