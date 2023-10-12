package ce.parser.domain

import ce.defs.DataType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import generators.obj.input.DataClass
import generators.obj.input.Node
import javax.inject.Inject

interface JsonToDataClassUseCase {
    operator fun invoke(json: String, preferedName: String): List<Node>
}

class JsonToDataClassUseCaseImpl @Inject constructor(): JsonToDataClassUseCase {

    override fun invoke(json: String, preferedName: String): List<Node> {
        val result = mutableListOf<Node>()
        val mapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        val node = mapper.readTree(json)
        val dataClass = DataClass(preferedName, null)

        node.fieldNames().forEach { childName ->
            val child = node.get(childName)
            val nodeType = child.nodeType

            when  {
                child.isBoolean -> dataClass.field(childName, DataType.bool)
                child.isDouble -> dataClass.field(childName, DataType.float64)
                child.isFloat -> dataClass.field(childName, DataType.float32)
                child.isTextual -> dataClass.field(childName, DataType.string(true))
                child.isLong -> dataClass.field(childName, DataType.int64)
                child.isNumber -> dataClass.field(childName, DataType.int32)
//                child.isArray
//                child.isObject
            }
        }
        result.add(dataClass)
        return result
    }
}