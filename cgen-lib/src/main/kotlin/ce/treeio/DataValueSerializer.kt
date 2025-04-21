package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
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
        if (!value.isDefined()) {
            return null
        } else {
            return value.simple.toString()
        }
    }

    fun fromString(value: String, dataType: DataType) : DataValue {
        if (value.isEmpty()) {
            return DataValueImpl.NotDefinedValue
        }
        val result = when (dataType) {
            DataType.VOID -> DataValueImpl.NotDefinedValue
            DataType.int8 -> DataValueImpl(simple = value.toByte())
            DataType.int16 -> DataValueImpl(simple = value.toShort())
            DataType.int32 -> DataValueImpl(simple = value.toInt())
            DataType.int64 -> DataValueImpl(simple = value.toLong())
            DataType.uint8 -> DataValueImpl(simple = value.toUByte())
            DataType.uint16 -> DataValueImpl(simple = value.toUShort())
            DataType.uint32 -> DataValueImpl(simple = value.toUInt())
            DataType.uint64 -> DataValueImpl(simple = value.toULong())
            DataType.float32, DataType.float64, DataType.float128 -> DataValueImpl(simple = value.toDouble())
            is DataType.string -> DataValueImpl(simple = value)
            DataType.bool -> DataValueImpl(simple = value.toBoolean())
            else -> throw IllegalStateException("Unsupported dataValue for data type $dataType")
        }
        return result
    }
}