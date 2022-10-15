package generators.obj.input

import ce.defs.DataType

open class DataClass(
    name: String,
    parent: Node,
) : Block(name, parent) {
    fun field(name: String, type : DataType, value: Any? = NotDefined) {
        subs.add(DataField(name, this, type, value))
    }
}
