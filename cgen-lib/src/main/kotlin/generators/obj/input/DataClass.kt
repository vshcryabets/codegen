package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue

data class DataClass(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
) : Block {

    fun instance(): DataValue {
        return DataValue(simple = "ASD")
    }

    fun instance(map: Map<Any,Any?>): DataValue {
        map.forEach { t, u ->
            println("Map key ${t.javaClass} $t = $u")
        }
        println("Map = $map")
        val newInstance = NewInstance("newInstance", type = DataType.custom(this))
        return DataValue(simple = "ASD")
    }

    fun addstaticfield(name: String, type: DataType, value: Any?) {
        addSub(DataField(name, type, DataValue(simple = value), static = true))
    }

    fun field(name: String, type: DataType, value: Any?) {
        addSub(DataField(name, type, DataValue(simple = value)))
    }

    fun field(name: String, type: DataType) {
        addSub(DataField(name, type, DataValue.NotDefinedValue))
    }

    override fun addBlockComment(value: String) {
        this.addBlockCommentExt(value)
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}
