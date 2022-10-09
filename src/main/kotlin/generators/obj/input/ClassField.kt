package generators.obj.input

import ce.defs.DataType

object NotDefined

open class ClassField(
    name: String,
    parent: Node,
    val type: DataType,
    var value : Any? = NotDefined
)  : Leaf(name, parent)

open class Output(name: String, parent: Node, type: DataType) : ClassField(
    name,
    parent,
    type
)

open class Input(name: String, parent: Node, type: DataType, value : Any? = NotDefined) : ClassField(
    name,
    parent,
    type,
    value
)

open class ConstantDesc(name: String, parent: Node, type: DataType, value : Any? = NotDefined) : ClassField(
    name,
    parent,
    type,
    value
)