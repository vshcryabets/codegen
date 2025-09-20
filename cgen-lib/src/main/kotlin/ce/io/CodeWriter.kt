package ce.io

interface CodeWriter {
    fun write(str: String) : CodeWriter
    fun writeNl() : CodeWriter
    fun writeNlIfNotEmpty() : CodeWriter

    fun setIndent(str: String) : CodeWriter
    fun setNewLine(str: String)
}