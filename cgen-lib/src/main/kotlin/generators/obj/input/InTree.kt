package generators.obj.input

object TreeRoot : Node {

    override val subs: MutableList<Leaf> = mutableListOf()

    override val name: String = "ROOT"

    override var parent: Node? = null

    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf {
        TODO("Not yet implemented")
    }
}


fun <T: Leaf> T.getParentPath(): String = parent?.getPath() ?: ""

fun <T: Leaf> T.getPath(): String {
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

interface Leaf {
    val name: String
    var parent: Node?

    fun copyLeaf(parent: Node? = null, copySubs: Boolean = true): Leaf
}
