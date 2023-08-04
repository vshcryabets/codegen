package generators.obj.out

import ce.defs.Target
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.TreeRoot
import generators.obj.input.copyLeafExt

data class ProjectOutput(
    val target: Target,
    override val name: String = "/",
    override var parent: Node? = TreeRoot,
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }}