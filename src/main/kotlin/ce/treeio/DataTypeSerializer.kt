package ce.treeio

import ce.defs.DataType
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer

class DataTypeSerializer : JsonSerializer<DataType>() {

    companion object {
        val DATATYPES_MAP = hashMapOf(
            DataType.int8 to "int8",
            DataType.VOID to "void",
            DataType.bool to "bool",
            DataType.float128 to "float128",
            DataType.float32 to "float32",
            DataType.float64 to "float64",
            DataType.int16 to "int16",
            DataType.int32 to "int32",
            DataType.int64 to "int64",
            DataType.string to "string",
            DataType.uint16 to "uint16",
            DataType.uint32 to "uint32",
            DataType.uint64 to "uint64",
            DataType.uint8 to "uint8",
        )
        val DATATYPES_REVERSE_MAP = mutableMapOf<String, DataType>()
    }

    init {
        if (DATATYPES_REVERSE_MAP.isEmpty()) {
            DATATYPES_MAP.forEach {
                DATATYPES_REVERSE_MAP[it.value] = it.key
            }
        }
    }

    override fun serializeWithType(
        value: DataType?,
        gen: JsonGenerator?,
        serializers: SerializerProvider?,
        typeSer: TypeSerializer?
    ) {

    }

    override fun serialize(value: DataType, gen: JsonGenerator, serializers: SerializerProvider) {
        val typeStr = stringValue(value)
        gen.writeString(typeStr)
    }

    fun stringValue(value: DataType): String {
        if (DATATYPES_MAP.containsKey(value))
            return DATATYPES_MAP[value]!!

        val typeStr = when (value) {
            is DataType.array -> "arrayOfXXXXX"
            is DataType.pointer -> "pointerXXX"
            is DataType.promise -> "FlowXXX"
            is DataType.userClass -> value.path
            else -> "UNK" + value.toString()
        }
        return typeStr
    }

    fun fromStringValue(value: String): DataType {
        if (DATATYPES_REVERSE_MAP.containsKey(value))
            return DATATYPES_REVERSE_MAP[value]!!
        throw IllegalStateException("Not supported $value")
    }
}