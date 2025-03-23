package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class DataValueSerializer : JsonSerializer<DataValue>() {
    override fun serialize(value: DataValue, gen: JsonGenerator, serializers: SerializerProvider) {
        val string = stringValue(value)
        if (string == null) {
            gen.writeNull()
        } else {
            gen.writeString(string)
        }
    }

    fun stringValue(value: DataValue) : String? {
        if (value.isComplex) {
            throw IllegalStateException("Complex data value cannot be serialized")
        }
        if (value.notDefined()) {
            return null
        } else {
            return value.simple.toString()
        }
    }

    fun fromString(value: String, dataType: DataType) : DataValue {
        if (value.isEmpty()) {
            return DataValue.NotDefinedValue
        }
        val result = when (dataType) {
            DataType.VOID -> DataValue.NotDefinedValue
            DataType.int8 -> DataValue(simple = value.toByte())
            DataType.int16 -> DataValue(simple = value.toShort())
            DataType.int32 -> DataValue(simple = value.toInt())
            DataType.int64 -> DataValue(simple = value.toLong())
            DataType.uint8 -> DataValue(simple = value.toUByte())
            DataType.uint16 -> DataValue(simple = value.toUShort())
            DataType.uint32 -> DataValue(simple = value.toUInt())
            DataType.uint64 -> DataValue(simple = value.toULong())
            DataType.float32, DataType.float64, DataType.float128 -> DataValue(simple = value.toDouble())
            is DataType.string -> DataValue(simple = value)
            DataType.bool -> DataValue(simple = value.toBoolean())
            else -> throw IllegalStateException("Unsupported datatValue for data type $dataType")
        }
        return result
    }
}