package generators.java

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.copyNodeExt
import generators.obj.syntaxParseTree.ClassData

data class JavaClassData(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }


}