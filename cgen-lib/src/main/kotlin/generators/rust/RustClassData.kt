package generators.rust

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.copyLeafExt

data class RustClassData(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf()
) : generators.obj.syntaxParseTree.ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): RustClassData =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}