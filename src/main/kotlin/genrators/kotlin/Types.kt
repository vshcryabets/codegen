package genrators.kotlin

import genrators.obj.DataType

object Types {
    fun typeTo(file: genrators.obj.file.ClassData,
               type: DataType
    ) : String =
        when (type) {
            DataType.VOID -> "void"
            DataType.uint16 -> "Int"
            DataType.string -> {
                file.addInclude("<string>")
                "std::string"
            }
            else -> "QQQQ"
        }
}