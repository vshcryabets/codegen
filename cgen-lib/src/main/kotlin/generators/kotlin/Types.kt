package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import generators.obj.input.findOrCreateSub
import generators.obj.input.getParentPath
import generators.obj.input.getPath
import generators.obj.out.FileData
import generators.obj.out.ImportsBlock

object Types {
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
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name // TODO nullable check ?
            else -> "ktQQTP_array_$type"
        }

    fun typeTo(file: FileData,
               type: DataType
    ) : String {
        when (type) {
            is DataType.custom ->
                file.findOrCreateSub(ImportsBlock::class.java)
                    .addInclude("${type.block.getParentPath()}.${type.block.name}");
            else -> {}
        }
        val qwe: ULong
        return when (type) {
            DataType.VOID -> "void"
            DataType.int8,DataType.int8Nullable -> "Byte"
            DataType.int16,DataType.int16Nullable -> "Short"
            DataType.int32,DataType.int32Nullable -> "Int"
            DataType.int64,DataType.int64Nullable -> "Long"
            DataType.uint8,DataType.uint8Nullable -> "UByte"
            DataType.uint16,DataType.uint16Nullable -> "UShort"
            DataType.uint32,DataType.uint32Nullable -> "UInt"
            DataType.uint64,DataType.uint64Nullable -> "ULong"
            DataType.float32,DataType.float32Nullable -> "Float"
            DataType.float64,DataType.float64Nullable -> "Double"
            DataType.bool,DataType.boolNullable -> "Boolean"
            is DataType.string -> "String"
            is DataType.array -> getArrayType(type.elementDataType)
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name
            is DataType.userClassTest2 -> type.node.getPath()
            else -> "ktQQTP_$type"
        } + (if (type.canBeNull) "?" else "")
    }

    fun toValue(type: DataType, value: DataValue) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.int32, DataType.int64,
            DataType.uint8, DataType.uint16, DataType.uint32, DataType.uint64 -> value.simple.toString()
            DataType.float32 -> value.simple.toString() + "f"
            DataType.float64 -> value.simple.toString()
            DataType.bool -> value.simple.toString()
            is DataType.string -> "\"${value.simple}\""
            else -> "QQVAL_$type"
        }
}