package generators.cpp

import ce.defs.DataType
import generators.kotlin.KotlinClassData
import generators.obj.out.ClassData
import generators.obj.out.ClassHeader

object Types {
    fun typeTo(file: CppClassData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.uint16 -> "uint16_t"
            DataType.string -> {
                file.addInclude("<string>")
                "std::string"
            }
            else -> "QQQQ"
        }
    fun toValue(classData: CppClassData, type: DataType, value: Any?) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.int16, DataType.uint16 -> value.toString()
            DataType.float32 -> value.toString() + "f"
            DataType.float64 -> value.toString()
            DataType.string -> {
                "\"${value}\""
            }
            else -> "QQQQ"
        }

}