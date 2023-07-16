package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import generators.obj.out.ClassData
import generators.obj.out.FileData
import generators.obj.out.ImportLeaf
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
            else -> "QQTP_array_$type"
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
        return when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "Byte"
            DataType.int16 -> "Short"
            DataType.int32 -> "Int"
            DataType.int64 -> "Long"
            DataType.uint16 -> "Int"
            DataType.uint32 -> "Long"
            DataType.float32 -> "Float"
            DataType.float64 -> "Double"
            DataType.bool -> "Boolean"
            is DataType.string -> "String"
            is DataType.array -> getArrayType(type.elementDataType)
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name
            else -> "QQTP_$type"
        } + (if (type.canBeNull) "?" else "")
    }

    fun toValue(classData: ClassData, type: DataType, value: DataValue) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.int32,
            DataType.uint16, DataType.uint32 -> value.value.toString()
            DataType.float32 -> value.value.toString() + "f"
            DataType.float64 -> value.value.toString()
            DataType.bool -> value.value.toString()
            is DataType.string -> "\"${value.value}\""
            else -> "QQVAL_$type"
        }
}