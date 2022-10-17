package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined

open class DataClass(
    name: String,
    parent: Node,
) : Block(name, parent) {
    fun field(name: String, type : DataType, value: Any? = NotDefined) {
        subs.add(DataField(name, this, type, DataValue(value)))
    }
}
