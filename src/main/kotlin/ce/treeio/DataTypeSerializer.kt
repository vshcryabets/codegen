package ce.treeio

import ce.defs.DataType
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import generators.obj.input.Leaf

class DataTypeSerializer : JsonSerializer<DataType>() {
    override fun serialize(value: DataType, gen: JsonGenerator, serializers: SerializerProvider) {
        val typeStr = when (value) {
            is DataType.int8 -> "int8"
            DataType.VOID -> "void"
            is DataType.array -> "arrayOfXXXXX"
            DataType.bool -> "bool"
            DataType.float128 -> "float128"
            DataType.float32 -> "float32"
            DataType.float64 -> "float64"
            DataType.int16 -> "int16"
            DataType.int32 -> "int32"
            DataType.int64 -> "int64"
            DataType.string -> "string"
            DataType.uint16 -> "uint16"
            DataType.uint32 -> "uint32"
            DataType.uint64 -> "uint64"
            DataType.uint8 -> "uitn8"
            is DataType.pointer -> "pointerXXX"
        }
        gen.writeString(typeStr)
    }
}