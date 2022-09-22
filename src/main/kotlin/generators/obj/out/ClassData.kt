package generators.obj.out

import generators.obj.input.TreeRoot

open class ClassData(val namespace : String, name: String, parent : OutNode) : OutNode(name, parent) {
    val classStart = StringBuilder()
    val classComment = StringBuilder()
    val classDefinition = StringBuilder()
    val classEnd = StringBuilder()
}

