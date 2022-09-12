package generators.obj.input

import ce.defs.DataType


open class ConstantsBlock(
    name: String,
    namespace: String,
    val constants: ArrayList<ClassField> = ArrayList(),
    var defaultDataType: DataType = DataType.VOID
) : Block(name, namespace, emptyList()) {

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any? = null) {
        constants.add(ClassField(name, defaultDataType, value))
    }

    fun add(name: String, type : DataType, value: Any? = null) {
        constants.add(ClassField(name, type, value))
    }
}