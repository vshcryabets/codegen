package genrators.obj

import ce.defs.DataType

data class MethodDescription(
    val name: String,
    val result: DataType,
    val arguments: List<MethodArgument>
)
