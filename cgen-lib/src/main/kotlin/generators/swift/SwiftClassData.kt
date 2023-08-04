package generators.swift

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

data class SwiftClassData(
    override val name: String,
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : generators.obj.out.ClassData {
    override fun copyLeaf(parent: Node?) = copyLeafExt(parent) { this.copy(subs = mutableListOf()) }
}