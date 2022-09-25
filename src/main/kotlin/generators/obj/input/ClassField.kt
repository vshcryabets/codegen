package generators.obj.input

import ce.defs.DataType

object NotDefined

open class ClassField(
    name: String,
    parent: Node,
    val type: DataType,
    var value : Any? = NotDefined
)  : Leaf(name, parent)