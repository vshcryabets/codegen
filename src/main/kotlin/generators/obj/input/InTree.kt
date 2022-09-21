package generators.obj.input

object TreeRoot : InNode("ROOT", null)
open class InLeaf(val name : String, val parent: InNode?) {
    fun getParentPath(): String = parent?.getPath() ?: ""
    fun getPath(): String {
        if (parent == null) {
            return ""
        }
        val parentPath = getParentPath()
        return if (parentPath.isNotEmpty()) {
            "$parentPath.$name"
        } else {
            name
        }
    }
}

open class InNode(name: String, parent: InNode?, val subs: MutableList<InLeaf> = mutableListOf()) : InLeaf(name, parent)

class Namespace(name: String, parent: InNode) : InNode(name, parent) {
    fun getNamespace(name: String): Namespace {
        if (name.isEmpty()) {
            return this
        }
        val pointPos = name.indexOf('.')
        val searchName : String
        val endPath : String
        if (pointPos < 0) {
            searchName = name
            endPath = ""
        }
        else  {
            searchName = name.substring(0, pointPos)
            endPath = name.substring(pointPos + 1)
        }

        subs.forEach {
            if (it is Namespace) {
                if (it.name == searchName) {
                    return it.getNamespace(endPath)
                }
            }
        }
        val newNamaspace = Namespace(searchName, this)
        subs.add(newNamaspace)
        return newNamaspace.getNamespace(endPath)
    }
}