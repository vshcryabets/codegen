package generators.kotlin

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.copyNodeExt

data class KotlinClassData(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf()
) : generators.obj.syntaxParseTree.ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    override fun toString(): String = "KotlinClassData $name"
    override fun hashCode(): Int = name.hashCode() xor subs.hashCode()

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }

}