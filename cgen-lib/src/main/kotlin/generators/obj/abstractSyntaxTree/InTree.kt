package generators.obj.abstractSyntaxTree

object TreeRoot : Node {

    override val subs: MutableList<Leaf> = mutableListOf()

    override val name: String = "ROOT"

    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node = this
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}


fun <T: Leaf> T.getParentPath(): String = getParent2()?.getPath() ?: ""

fun <T: Leaf> T.getPath(): String {
    if (getParent2() == null) {
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

    fun getParent2(): Node?
    fun setParent2(parent:Node?)
    fun copyLeaf(parent: Node? = null, copySubs: Boolean = true): Leaf
}
