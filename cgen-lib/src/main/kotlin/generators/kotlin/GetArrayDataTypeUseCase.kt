package generators.kotlin

import ce.defs.DataType

class GetArrayDataTypeUseCase {
    fun getArrayType(type: DataType): String =
        when (type) {
            DataType.int8, DataType.uint8 -> "ByteArray"
            DataType.int16 -> "ShortArray"
            DataType.int32 -> "IntArray"
            DataType.int64 -> "LongArray"
            DataType.uint16 -> "ShortArray"
            DataType.uint32 -> "IntArray"
            DataType.float32 -> "FloatArray"
            DataType.float64 -> "DoubleArray"
            is DataType.string -> "String[]"
            is DataType.userClass -> "Array<${type.path}>"
            is DataType.custom -> "Array<${type.block.name}>"
            else -> "ktQQTP_array_$type"
        }
}