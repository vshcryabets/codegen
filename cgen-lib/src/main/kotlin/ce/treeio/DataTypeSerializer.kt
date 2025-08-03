package ce.treeio

import ce.defs.DataType
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import generators.obj.input.getPath

class DataTypeSerializer : JsonSerializer<DataType>() {

    companion object {
        val STRING_NULLABLE = "stringNullable"
        val STRING = "string"
        val PREFIX_ARRAY_OF = "arrayOf_"
        val PREFIX_POINTER_TO = "pointerTo_"
        val PREFIX_FLOW_OF = "flowOf_"
        val PREFIX_CLASS = "class_"

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
            DataType.uint16 to "uint16",
            DataType.uint32 to "uint32",
            DataType.uint64 to "uint64",
            DataType.uint8 to "uint8"
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
            is DataType.array -> PREFIX_ARRAY_OF + stringValue(value.elementDataType)
            is DataType.pointer -> PREFIX_POINTER_TO + stringValue(value.subType)
            is DataType.promise -> PREFIX_FLOW_OF + stringValue(value.elementDataType)
            is DataType.userClass -> PREFIX_CLASS + value.path
            is DataType.userClassTest2 -> PREFIX_CLASS + value.node.getPath()
            is DataType.string -> if (value.canBeNull)
                STRING_NULLABLE
            else
                STRING
            else -> "UNK" + value.toString()
        }
        return typeStr
    }

    fun fromStringValue(value: String): DataType {
        if (DATATYPES_REVERSE_MAP.containsKey(value))
            return DATATYPES_REVERSE_MAP[value]!!
        if (value.startsWith(PREFIX_ARRAY_OF)) {
            return DataType.array(fromStringValue(value.substring(PREFIX_ARRAY_OF.length)))
        }
        if (value == STRING) {
            return DataType.string()
        }
        if (value == STRING_NULLABLE) {
            return DataType.string(canBeNull = true)
        }
        throw IllegalStateException("Not supported data type=\"$value\"")
    }
}