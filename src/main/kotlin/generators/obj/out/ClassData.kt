package generators.obj.out

import generators.obj.input.Node

open class ClassData(name: String, parent : Node) : Node(name, parent) {
    val classDefinition = StringBuilder()
}

