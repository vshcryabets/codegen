package generators.cpp

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import generators.obj.input.findOrCreateSub
import generators.obj.out.FileData
import generators.obj.out.ImportsBlock

object Types {
    fun typeTo(file: FileData,
               type: DataType
    ) : String {
        if (type is DataType.string) {
            file.findOrCreateSub(ImportsBlock::class.java).addInclude("<string>")
        }
        if (type.isInteger()) {
            file.findOrCreateSub(ImportsBlock::class.java).addInclude("<cstdint>")
        }
        return when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "int8_t"
            DataType.int16 -> "int16_t"
            DataType.int32 -> "int32_t"
            DataType.int64 -> "int64_t"
            DataType.uint8 -> "uint8_t"
            DataType.uint16 -> "uint16_t"
            DataType.uint32 -> "uint32_t"
            DataType.uint64 -> "uint64_t"
            is DataType.string -> "std::string"

            DataType.float32 -> "float"
            DataType.float64 -> "double"
            else -> "cxxQQTP_$type"
        }
    }

    fun toValue(type: DataType, value: DataValue) : DataValue =
        when (type) {
            DataType.VOID -> DataValueImpl(name = "void")
            DataType.int8, DataType.int16, DataType.int32, DataType.int64,
            DataType.uint8, DataType.uint16, DataType.uint32, DataType.uint64 -> DataValueImpl(name = value.simple.toString())
            DataType.float32 -> DataValueImpl(name = value.simple.toString() + "f")
            DataType.float64 -> DataValueImpl(name = value.simple.toString())
            is DataType.string -> {
                DataValueImpl(name = value.simple.toString())
            }
            else -> DataValueImpl(name = "QQVL_$type")
        }

}