package generators.obj.out

import ce.defs.Target
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.TreeRoot
import generators.obj.input.copyLeafExt

data class ProjectOutput(
    val target: Target,
    override val name: String = "/",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): ProjectOutput =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}