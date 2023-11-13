package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined

data class ConstantsBlock(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
    var defaultDataType: DataType = DataType.VOID
) : Block {
    override fun toString() = name

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any? = NotDefined) : ConstantDesc =
        addSub(ConstantDesc(name, defaultDataType, DataValue(value)))

    fun add(name: String, type : DataType, value: Any? = NotDefined) =
        addSub(ConstantDesc(name, type, DataValue(value)))

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