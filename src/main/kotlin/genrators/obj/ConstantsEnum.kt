package genrators.obj

data class ConstantsEnum(
    val name: String,
    val namespace: String,
    val constants: List<ClassField>
)
