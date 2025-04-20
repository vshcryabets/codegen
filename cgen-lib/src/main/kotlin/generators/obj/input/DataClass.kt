package generators.obj.input

import ce.defs.DataType
import ce.defs.NotDefined

data class DataClass(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
) : Block {

    fun instance() = NewInstance("newInstance", type = DataType.custom(this@DataClass))

    fun instance(map: Map<Any, Any?>): NewInstance {
        return NewInstance("newInstance", type = DataType.custom(this@DataClass)).apply {
            map.forEach { t, u ->
                argument(t.toString(), DataType.Unknown, u)
            }
        }
    }

    fun addstaticfield(name: String, type: DataType, value: Any?) {
        addSub(DataField(name, static = true).apply {
            setType(type)
            setValue(value)
        })
    }

    fun field(name: String, type: DataType, value: Any?) {
        addSub(DataField(name))
    }

    fun field(name: String, type: DataType) = field(name, type, NotDefined)

    override fun addBlockComment(value: String) {
        this.addBlockCommentExt(value)
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) {
        this.parent = parent
    }
}
