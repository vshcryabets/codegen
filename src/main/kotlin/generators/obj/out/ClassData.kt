package generators.obj.out

import generators.obj.input.Node

open class ClassData(name: String, parent : OutNode) : Node(name, parent) {
    val classDefinition = StringBuilder()
}

