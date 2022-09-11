package generators.obj.out

open class FileData {
    val initialComments = StringBuilder()
    var headers = StringBuilder()
    var fullOutputFileName : String = ""
    var currentTabLevel : Int = 0
    var end = StringBuilder()
    val namespaces = mutableMapOf<String, Namespace>()
    val includes = HashSet<String>()

    fun addInclude(name: String) {
        includes.add(name)
    }

    fun appendHeaderLine(s: String) {
        headers.append(s)
        headers.append('\n')
    }

    open fun getInitialComments() : String = initialComments.toString()
    open fun getHeaders(): String = headers.toString()
}