package generators.obj.input

import ce.defs.DataType

open class ConstantsBlock(
    name: String,
    parent: Node,
    var defaultDataType: DataType = DataType.VOID
) : Block(name, parent) {

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any = NotDefined) : ConstantDesc =
        addSub(ConstantDesc(name, this, defaultDataType, value))

    fun add(name: String, type : DataType, value: Any = NotDefined) =
        addSub(ConstantDesc(name, this, type, value))
}
