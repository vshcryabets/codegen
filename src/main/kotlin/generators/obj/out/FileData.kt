package generators.obj.out

import generators.obj.input.Namespace
import generators.obj.input.Node

open class FileData(name: String, parent: Node) : Namespace(name, parent) {
    var headers = StringBuilder()
    var end = StringBuilder()

    init {
        subs.add(CommentsBlock(this))
    }

    fun appendHeaderLine(s: String) {
        headers.append(s)
        headers.append('\n')
    }

    open fun getHeaders(): String = headers.toString()
}