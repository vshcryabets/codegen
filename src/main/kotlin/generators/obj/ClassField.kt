package generators.obj

import ce.defs.DataType

data class ClassField (
    val name: String,
    val type: DataType,
    var value : Any? = null
)