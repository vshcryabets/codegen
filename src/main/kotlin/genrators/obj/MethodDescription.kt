package genrators.obj

data class MethodDescription(
    val name: String,
    val result: DataType,
    val arguments: List<MethodArgument>
)
