package generators.obj.out

import generators.obj.input.Namespace
import generators.obj.input.Node

open class FileData(
    name: String,
    parent: Node,

) : Namespace(name, parent) {
    var isDirty: Boolean = false
        private set
    fun resetDirtyFlag() {
        isDirty = false
    }
    fun setDirtyFlag() {
        isDirty = true
    }
}