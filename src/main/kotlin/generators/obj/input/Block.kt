package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue

open class Block(
    name: String,
    parent: Node
) : Node(name, parent) {
//    val classComment = StringBuilder()
    var sourceFile = ""
    var outputFile = ""
    var objectBaseFolder = ""

    fun addClassField(name: String, type : DataType, value: DataValue = NotDefinedValue) {
        subs.add(DataField(name, this, type, value))
    }
}
