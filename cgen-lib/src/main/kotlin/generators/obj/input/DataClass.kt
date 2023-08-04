package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined

data class DataClass(
    name: String,
    parent: Node,
) : Block(name, parent) {
    fun field(name: String, type : DataType, value: Any? = NotDefined) {
        addSub(DataField(name, type, DataValue(value)))
    }
}
