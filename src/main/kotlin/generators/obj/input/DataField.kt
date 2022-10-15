package generators.obj.input

import ce.defs.DataType

object NotDefined

open class DataField(
    name: String,
    parent: Node? = null,
    val type: DataType,
    var value : Any? = NotDefined
)  : Leaf(name, parent)

open class Output(name: String, parent: Node, type: DataType) : DataField(name, parent, type)
open class Input(name: String, parent: Node, type: DataType, value : Any? = NotDefined) : DataField(
    name,
    parent,
    type,
    value
)

open class ConstantDesc(name: String, parent: Node, type: DataType, value : Any? = NotDefined) : DataField(
    name,
    parent,
    type,
    value
)