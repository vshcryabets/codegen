package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue


//open class DataField(
//    override val name: String,
//    val type: DataType,
//    var value: DataValue = NotDefinedValue,
//    override var parent: Node? = null,
//) : Leaf

interface Field : Leaf {
    val type: DataType
    var value: DataValue// = NotDefinedValue
}

data class DataField(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field

data class Output(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field

data class OutputReusable(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field

data class Input(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field

data class ConstantDesc(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field