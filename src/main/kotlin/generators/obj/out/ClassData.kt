package generators.obj.out

import generators.obj.input.TreeRoot

open class ClassData(name: String, parent : OutNode) : OutNode(name, parent) {
    val classDefinition = StringBuilder()
}

