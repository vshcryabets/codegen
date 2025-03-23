package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue

data class NewInstance(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    val type: DataType,
) : Node {

    fun argument(name: String, type: DataType, value: Any?) {
        addSub(DataField(name, type, DataValue(simple = value)))
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}