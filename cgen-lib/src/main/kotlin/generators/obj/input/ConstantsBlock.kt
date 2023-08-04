package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined

data class ConstantsBlock(
    name: String,
    parent: Node? = null,
    var defaultDataType: DataType = DataType.VOID
) : Block(name, parent) {

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any? = NotDefined) : ConstantDesc =
        addSub(ConstantDesc(name, defaultDataType, DataValue(value)))

    fun add(name: String, type : DataType, value: Any? = NotDefined) =
        addSub(ConstantDesc(name, type, DataValue(value)))
}
