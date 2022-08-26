package genrators.obj

data class InterfaceDescription(
    val name: String,
    val namespace: String,
    val publicMethods: List<MethodDescription>
)
