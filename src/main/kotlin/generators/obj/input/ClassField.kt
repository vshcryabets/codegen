package generators.obj.input

import ce.defs.DataType

object NotDefined;

open class ClassField(
    name: String,
    val type: DataType,
    var value : Any? = NotDefined
)  : InLeaf(name)