package generators.swift

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

data class SwiftClassData(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf()
) : generators.obj.out.ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): SwiftClassData =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}