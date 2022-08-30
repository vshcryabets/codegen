package generators.obj

import ce.defs.DataType
import generators.obj.file.ClassData

data class ConstantsEnum(
    val name: String,
    val namespace: String,
    val constants: ArrayList<ClassField> = ArrayList(),
    var defaultDataType: DataType = DataType.VOID
) {
    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any? = null) {
        constants.add(ClassField(name, defaultDataType, value))
    }
}
