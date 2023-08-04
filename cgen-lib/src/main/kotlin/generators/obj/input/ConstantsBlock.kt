package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined

data class ConstantsBlock(
    override val name: String,
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
    var defaultDataType: DataType = DataType.VOID
) : Block {
    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any? = NotDefined) : ConstantDesc =
        addSub(ConstantDesc(name, defaultDataType, DataValue(value)))

    fun add(name: String, type : DataType, value: Any? = NotDefined) =
        addSub(ConstantDesc(name, type, DataValue(value)))

    override fun copyLeaf(parent: Node?): ConstantsBlock =
        this.copyLeafExt(parent) {
            this.copy(
                subs = mutableListOf(),
                parent = parent
            )
        }
}