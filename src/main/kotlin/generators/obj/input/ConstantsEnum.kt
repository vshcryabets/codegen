package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined


open class ConstantsEnum(
    name: String,
    parent: Node,
    var defaultDataType: DataType = DataType.VOID
) : Block(name, parent) {

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any = NotDefined) {
        addSub(DataField(name, defaultDataType, DataValue(value)))
    }
}
