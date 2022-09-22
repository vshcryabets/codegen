package generators.cpp

import ce.defs.DataType
import generators.obj.out.FileData
import generators.obj.out.ImportLeaf
import generators.obj.out.ImportsBlock

object Types {
    fun typeTo(file: FileData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "int8_t"
            DataType.int16 -> "int16_t"
            DataType.uint16 -> "uint16_t"
            DataType.uint32 -> "uint32_t"
            DataType.string -> {
                file.findSub(ImportsBlock::class.java).addInclude("<string>")
                "std::string"
            }
            DataType.float32 -> "float"
            DataType.float64 -> "double"
            else -> "QQTP_$type"
        }
    fun toValue(classData: CppHeaderData, type: DataType, value: Any?) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.int32,
            DataType.uint8, DataType.uint16, DataType.uint32 -> value.toString()
            DataType.float32 -> value.toString() + "f"
            DataType.float64 -> value.toString()
            DataType.string -> {
                "\"${value}\""
            }
            else -> "QQVL_$type"
        }

}