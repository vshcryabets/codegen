package genrators.cpp

import genrators.obj.DataType
import genrators.obj.file.ClassHeader

object Types {
    fun typeTo(file: ClassHeader,
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
}