package genrators.obj

import ce.defs.DataType

data class ClassField (
    val name: String,
    val type: DataType,
    val value : Any? = null
)