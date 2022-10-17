package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue


open class DataField(
    name: String,
    parent: Node? = null,
    val type: DataType,
    var value : DataValue = NotDefinedValue
)  : Leaf(name, parent)

open class Output(name: String, parent: Node, type: DataType) : DataField(name, parent, type, NotDefinedValue)
open class Input(name: String, parent: Node, type: DataType, value : DataValue) : DataField(
    name,
    parent,
    type,
    value
)

open class ConstantDesc(name: String, parent: Node, type: DataType, value : DataValue) : DataField(
    name,
    parent,
    type,
    value
)