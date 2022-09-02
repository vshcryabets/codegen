package generators.kotlin

import ce.defs.DataType

object Types {
    fun typeTo(file: generators.obj.out.ClassData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int16,
            DataType.uint16 -> "Int"
            DataType.float32 -> "Float"
            DataType.float64 -> "Double"
            DataType.string -> {
                file.addInclude("<string>")
                "std::string"
            }
            else -> "QQQQ"
        }

    fun toValue(classData: ClassData, type: DataType, value: Any?) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int16, DataType.uint16 -> value.toString()
            DataType.float32 -> value.toString() + "f"
            DataType.float64 -> value.toString()
            DataType.string -> {
                "\"${value}\""
            }
            else -> "QQQQ"
        }
}