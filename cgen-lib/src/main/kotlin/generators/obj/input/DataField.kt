package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue


open class DataField(
    name: String,
    val type: DataType,
    var value : DataValue = NotDefinedValue,
    parent: Node? = null,
)  : Leaf(name, parent)

open class Output(name: String, type: DataType) : DataField(name, type, NotDefinedValue, null)
open class OutputReusable(name: String, type: DataType) : DataField(name, type, NotDefinedValue, null)
open class Input(name: String, type: DataType, value : DataValue) : DataField(
    name,
    type,
    value
    )

open class ConstantDesc(name: String, parent: Node, type: DataType, value : DataValue) : DataField(
    name,
    type,
    value,
    parent,
    )