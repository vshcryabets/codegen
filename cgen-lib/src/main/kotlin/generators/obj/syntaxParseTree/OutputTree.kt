package generators.obj.syntaxParseTree

import ce.defs.Target
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.copyLeafExt

data class AstTree(
    val target: Target,
    override val name: String = "/",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): AstTree =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class OutputTree(
    val target: Target,
    override val name: String = "/",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): OutputTree =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class CodeStyleOutputTree(
    val target: Target,
    override val name: String = "/",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): CodeStyleOutputTree =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}