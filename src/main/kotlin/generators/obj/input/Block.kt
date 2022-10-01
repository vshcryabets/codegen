package generators.obj.input

import ce.defs.DataType

open class Block(
    name: String,
    parent: Node
) : Node(name, parent) {
    val classComment = StringBuilder()
    var sourceFile = ""
    var outputFile = ""
    var objectBaseFolder = ""

    fun addBlockComment(value : String) {
        classComment.append(value)
    }

    fun addClassField(name: String, type : DataType, value: Any? = NotDefined) {
        subs.add(ClassField(name, this, type, value))
    }
}
