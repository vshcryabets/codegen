package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue

object Types {
    fun toValue(type: DataType, value: DataValue) : DataValue =
        when (type) {
            DataType.VOID -> DataValue(name = "void")
            DataType.int8, DataType.int16, DataType.int32, DataType.int64,
            DataType.uint8, DataType.uint16, DataType.uint32, DataType.uint64 -> DataValue(name = value.simple.toString())
            DataType.float32 -> DataValue(name = value.simple.toString() + "f")
            DataType.float64 -> DataValue(name = value.simple.toString())
            DataType.bool -> DataValue(name = value.simple.toString())
            is DataType.string -> DataValue(name = value.simple.toString())
            is DataType.custom -> {
                if (!value.isComplex) {
                    DataValue(name = value.simple.toString())
                } else {
                    DataValue(name = "QQVAL_complex???")
                }

            }
            else -> DataValue(name = "QQVAL_$type")
        }
}