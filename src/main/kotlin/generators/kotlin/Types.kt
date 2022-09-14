package generators.kotlin

import ce.defs.DataType
import generators.obj.out.FileData

object Types {
    fun typeTo(file: FileData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "Byte"
            DataType.int16 -> "Short"
            DataType.int32 -> "Int"
            DataType.uint16 -> "Int"
            DataType.uint32 -> "Long"
            DataType.float32 -> "Float"
            DataType.float64 -> "Double"
            DataType.string -> {
                file.addInclude("<string>")
                "std::string"
            }
            else -> "QQTP_$type"
        }

    fun toValue(classData: KotlinClassData, type: DataType, value: Any?) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.int32,
            DataType.uint16 -> value.toString()
            DataType.float32 -> value.toString() + "f"
            DataType.float64 -> value.toString()
            DataType.string -> {
                "\"${value}\""
            }
            else -> "QQVAL_$type"
        }
}