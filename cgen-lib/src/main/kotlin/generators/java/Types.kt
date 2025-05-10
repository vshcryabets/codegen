package generators.java

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.IntValue
import generators.obj.input.getPath
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
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name // TODO nullable check ?
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
            DataType.int64 -> "long"
            DataType.uint16 -> "int"
            DataType.uint32 -> "long"
            DataType.float32 -> "float"
            DataType.float64 -> "double"
            DataType.bool -> "boolean"
            DataType.boolNullable -> "Boolean"
            is DataType.string -> "String"
            is DataType.array -> getArrayType(type.elementDataType)
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name
            is DataType.userClassTest2 -> type.node.getPath()
            else -> "javaQQTP_$type"
        }

    fun toValue(type: DataType, value: DataValue) : DataValue =
        when (type) {
            DataType.VOID -> DataValueImpl(name = "void")
            DataType.int8, DataType.int16, DataType.int32,
            DataType.uint16, DataType.uint32 -> DataValueImpl(name = value.simple.toString())
            DataType.float32 -> DataValueImpl(name = value.simple.toString() + "f")
            DataType.float64 -> DataValueImpl(name = value.simple.toString())
            is DataType.string -> {
                DataValueImpl(name = value.simple.toString())
            }
            DataType.int64 -> {
                if (value is IntValue) {
                    if (value.preferredRadix == 16) {
                        DataValueImpl(name = "0x${value.simple.toString(radix = value.preferredRadix)}L")
                    } else {
                        DataValueImpl(name = "${value.simple.toString()}L")
                    }
                } else {
                    DataValueImpl(name = "${value.simple.toString()}L")
                }
            }
            else -> DataValueImpl(name = "QQVAL_$type")
        }
}