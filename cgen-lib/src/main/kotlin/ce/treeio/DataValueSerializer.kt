package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue
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
        if (value.notDefined()) {
            return null
        } else {
            return value.value.toString()
        }
    }

    fun fromString(value: String, dataType: DataType) : DataValue {
        if (value.isEmpty()) {
            return NotDefinedValue
        }
        val result = when (dataType) {
            DataType.VOID -> NotDefinedValue
            DataType.int8 -> DataValue(value.toByte())
            DataType.int16 -> DataValue(value.toShort())
            DataType.int32 -> DataValue(value.toInt())
            DataType.int64 -> DataValue(value.toLong())
            DataType.uint8 -> DataValue(value.toUByte())
            DataType.uint16 -> DataValue(value.toUShort())
            DataType.uint32 -> DataValue(value.toUInt())
            DataType.uint64 -> DataValue(value.toULong())
            DataType.float32, DataType.float64, DataType.float128 -> DataValue(value.toDouble())
            DataType.string -> DataValue(value)
            DataType.bool -> DataValue(value.toBoolean())
            else -> throw IllegalStateException("Unsupported datatValue for data type $dataType")
        }
        return result
    }
}