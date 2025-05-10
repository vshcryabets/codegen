package generators.obj.input

import ce.defs.DataType

data class NewInstance(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    fun setType(type: DataType): NewInstance {
        subs.removeAll { it is DataType }
        addSub(TypeLeaf(type = type))
        return this
    }

    fun getType(): DataType {
        return subs.filterIsInstance<TypeLeaf>().lastOrNull()?.type ?: DataType.Unknown
    }

    fun argument(name: String, type: DataType, value: Any?): NewInstance {
        addSub(DataField(name).apply {
            setType(type)
            setValue(value)
        })
        return this
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}