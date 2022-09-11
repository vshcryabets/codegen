package generators.obj.out

open class Namespace(
    val name: String,
    val outputBlocks: MutableMap<String, ClassData> = mutableMapOf()
)