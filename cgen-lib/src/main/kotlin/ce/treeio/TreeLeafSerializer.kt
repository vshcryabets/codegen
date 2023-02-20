package ce.treeio

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import generators.obj.input.Leaf

class TreeLeafSerializer : JsonSerializer<Leaf>() {
    override fun serialize(value: Leaf, gen: JsonGenerator, serializers: SerializerProvider) {

    }
}