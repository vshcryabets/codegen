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

class Namespace(name: String, parent: InNode) : InNode(name, parent)