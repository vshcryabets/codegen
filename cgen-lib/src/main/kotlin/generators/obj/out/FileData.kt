package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

interface FileData: Node {
    var isDirty: Boolean
}

data class FileDataImpl(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false
) : FileData {

    override fun toString(): String = name

    override fun copyLeaf(parent: Node?, copySubs: Boolean): FileData =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}