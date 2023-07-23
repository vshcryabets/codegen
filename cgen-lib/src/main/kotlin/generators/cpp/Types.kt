package generators.cpp

import ce.defs.DataType
import ce.defs.DataValue
import generators.obj.out.ClassData
import generators.obj.out.FileData
import generators.obj.out.ImportsBlock

object Types {
    fun typeTo(file: FileData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "int8_t"
            DataType.int16 -> "int16_t"
            DataType.int32 -> "int32_t"
            DataType.int64 -> "int64_t"
            DataType.uint16 -> "uint16_t"
            DataType.uint32 -> "uint32_t"
            DataType.uint64 -> "uint64_t"
            is DataType.string -> {
                file.findOrCreateSub(ImportsBlock::class.java).addInclude("<string>")
                "std::string"
            }
            DataType.float32 -> "float"
            DataType.float64 -> "double"
            else -> "QQTP_$type"
        }
    fun toValue(file: ClassData, type: DataType, value: DataValue) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.int32,
            DataType.uint8, DataType.uint16, DataType.uint32 -> value.value.toString()
            DataType.float32 -> value.value.toString() + "f"
            DataType.float64 -> value.value.toString()
            is DataType.string -> {
                "\"${value}\""
            }
            else -> "QQVL_$type"
        }

}