package generators.obj.out

open class FileData(name: String, parent: OutNode) : OutNode(name, parent) {
    var headers = StringBuilder()
    var end = StringBuilder()
    val namespaces = mutableMapOf<String, OutNamespace>()

    init {
        subs.add(FileInitialCommentsBlock(this))
    }

    fun appendHeaderLine(s: String) {
        headers.append(s)
        headers.append('\n')
    }

    open fun getHeaders(): String = headers.toString()
}