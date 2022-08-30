package generators.obj

data class ClassDescription(
    val name: String,
    val namespace: String,
    val fields: List<ClassField>
)
