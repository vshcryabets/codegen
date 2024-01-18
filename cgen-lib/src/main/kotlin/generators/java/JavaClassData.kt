package generators.java

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt
import generators.obj.input.copyNodeExt
import generators.obj.out.ClassData

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