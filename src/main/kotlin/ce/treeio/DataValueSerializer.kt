package ce.treeio

import ce.defs.DataValue
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class DataValueSerializer : JsonSerializer<DataValue>() {
    override fun serialize(value: DataValue, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value.notDefined()) {
            gen.writeNull()
        } else {
            gen.writeString(value.value.toString())
        }
    }
}