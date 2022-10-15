package generators.obj.input

import ce.defs.DataType


open class ConstantsEnum(
    name: String,
    parent: Node,
    var defaultDataType: DataType = DataType.VOID
) : Block(name, parent) {

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any = NotDefined) {
        addSub(DataField(name, this, defaultDataType, value))
    }
}
