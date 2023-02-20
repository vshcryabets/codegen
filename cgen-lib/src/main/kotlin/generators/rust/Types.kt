package generators.rust

import ce.defs.DataType
import generators.obj.out.FileData
import generators.rust.RustClassData

object Types {
    fun typeTo(file: FileData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8 -> "i8"
            DataType.int16 -> "i16"
            DataType.uint16 -> "u16"
            DataType.uint32 -> "u32"
            DataType.float32 -> "f32"
            DataType.float64 -> "f64"
            else -> "QQTP_$type"
        }

    fun toValue(classData: RustClassData, type: DataType, value: Any?) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int16, DataType.uint16 -> value.toString()
            DataType.float32 -> value.toString()
            DataType.float64 -> value.toString()
            DataType.string -> {
                "\"${value}\""
            }
            else -> "QQVAL_$type"
        }
}