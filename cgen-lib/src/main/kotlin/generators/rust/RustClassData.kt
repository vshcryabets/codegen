package generators.rust

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

data class RustClassData(
    override val name: String,
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : generators.obj.out.ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }
}