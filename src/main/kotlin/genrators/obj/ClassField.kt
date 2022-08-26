package genrators.obj

data class ClassField (
    val name: String,
    val type: DataType,
    val value : Any? = null
)