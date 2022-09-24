package generators.kotlin

import ce.defs.DataType
import generators.obj.out.FileData

object Types {
    fun getArrayType(type: DataType): String =
        when (type) {
            DataType.int8 -> "ByteArray"
            DataType.int16 -> "ShortArray"
            DataType.int32 -> "IntArray"
            DataType.uint16 -> "IntArray"
            DataType.uint32 -> "LongArray"
            DataType.float32 -> "FloatArray"
            DataType.float64 -> "DoubleArray"
            DataType.string -> "String[]"
            else -> "QQTP_array_$type"
        }

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
            DataType.string -> "String"
            is DataType.array -> getArrayType(type.elementDataType)
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