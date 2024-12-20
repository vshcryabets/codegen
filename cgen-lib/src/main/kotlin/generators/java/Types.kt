package generators.java

import ce.defs.DataType
import ce.defs.DataValue
import generators.obj.out.FileData

object Types {
    fun getArrayType(type: DataType): String =
        when (type) {
            DataType.int8 -> "ByteArray"
            DataType.int16 -> "ShortArray"
            DataType.int32 -> "IntArray"
            DataType.int64 -> "LongArray"
            DataType.uint16 -> "IntArray"
            DataType.uint32 -> "LongArray"
            DataType.float32 -> "FloatArray"
            DataType.float64 -> "DoubleArray"
            is DataType.string -> "String[]"
            else -> "javaQQTP_array_$type"
        }

    fun typeTo(file: FileData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "byte"
            DataType.int16 -> "short"
            DataType.int32 -> "int"
            DataType.uint16 -> "int"
            DataType.uint32 -> "long"
            DataType.float32 -> "float"
            DataType.float64 -> "double"
            is DataType.string -> "string"
            is DataType.array -> getArrayType(type.elementDataType)
            else -> "javaQQTP_$type"
        }

    fun toValue(type: DataType, value: DataValue) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.int32,
            DataType.uint16, DataType.uint32 -> value.value.toString()
            DataType.float32 -> value.value.toString() + "f"
            DataType.float64 -> value.value.toString()
            is DataType.string -> {
                "\"${value.value}\""
            }
            else -> "QQVAL_$type"
        }
}