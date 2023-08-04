package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Namespace
import generators.obj.input.NamespaceImpl
import generators.obj.input.Node
import generators.obj.input.copyLeafExt
import generators.obj.input.getNamespaceExt

interface FileData: Node {
    var isDirty: Boolean
}

data class FileDataImpl(
    override val name: String,
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false
) : FileData {

    override fun copyLeaf(parent: Node?): FileDataImpl = this.copyLeafExt(parent) {
        this.copy(parent = parent, subs = mutableListOf())
    }
}