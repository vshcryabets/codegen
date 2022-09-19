package generators.obj.out

import generators.obj.out.nodes.FileInitialCommentsBlock

open class FileData : OutNode() {
    var headers = StringBuilder()
    var fullOutputFileName : String = ""
    var end = StringBuilder()
    val namespaces = mutableMapOf<String, Namespace>()

    init {
        leafs.add(FileInitialCommentsBlock())
    }

    fun appendHeaderLine(s: String) {
        headers.append(s)
        headers.append('\n')
    }

    open fun getHeaders(): String = headers.toString()
}