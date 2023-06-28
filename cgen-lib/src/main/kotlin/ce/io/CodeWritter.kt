package ce.io

interface CodeWritter {
    fun write(str: String) : CodeWritter
    fun writeNl() : CodeWritter
    fun writeNlIfNotEmpty() : CodeWritter

    fun setIndent(str: String) : CodeWritter
    fun setNewLine(str: String)
}