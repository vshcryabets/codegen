package ce.treeio

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
        if (!value.isDefined()) {
            return null
        } else {
            return value.simple.toString()
        }
    }
}