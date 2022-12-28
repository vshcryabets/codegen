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
        if (value.notDefined()) {
            return null
        } else {
            return value.value.toString()
        }
    }
}