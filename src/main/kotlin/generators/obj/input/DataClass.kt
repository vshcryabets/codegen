package generators.obj.input

import ce.defs.DataType

open class DataClass(
    name: String,
    namespace: String,
) : Block(name, namespace) {
    fun field(name: String, type : DataType, value: Any? = NotDefined) {
        leafs.add(ClassField(name, type, value))
    }
}
