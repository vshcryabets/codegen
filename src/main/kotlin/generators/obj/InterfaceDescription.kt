package generators.obj

import generators.obj.input.Block

class InterfaceDescription(
    name: String,
    namespace: String,
    val publicMethods: List<MethodDescription>
) : Block(name, namespace, emptyList())
