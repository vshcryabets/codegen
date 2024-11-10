package generators.swift

import ce.defs.DataType
import generators.obj.out.FileData

object Types {
    fun typeTo(file: FileData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "Int8"
            DataType.int16 -> "Int16"
            DataType.uint16 -> "UInt16"
            DataType.uint32 -> "UInt32"
            DataType.float32 -> "Float"
            DataType.float64 -> "Double"
//            DataType.string -> {
//                file.addInclude("<string>")
//                "std::string"
//            }
            else -> "swiftQQTP_$type"
        }

    fun toValue(classData: SwiftClassData, type: DataType, value: Any?) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.uint16 -> value.toString()
            DataType.float32 -> value.toString() + "f"
            DataType.float64 -> value.toString()
            is DataType.string -> {
                "\"${value}\""
            }
            else -> "QQVAL_$type"
        }
}