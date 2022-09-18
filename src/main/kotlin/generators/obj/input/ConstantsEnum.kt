package generators.obj.input

import ce.defs.DataType


open class ConstantsEnum(
    name: String,
    namespace: String,
    var defaultDataType: DataType = DataType.VOID
) : Block(name, namespace) {

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any = NotDefined) {
        leafs.add(ClassField(name, defaultDataType, value))
    }
}
