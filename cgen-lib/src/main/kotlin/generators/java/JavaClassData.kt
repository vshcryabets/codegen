package generators.java

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt
import generators.obj.out.ClassData

data class JavaClassData(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }
}