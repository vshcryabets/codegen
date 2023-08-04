package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue
import generators.cpp.CompilerDirective


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
) : Field {
    override fun copy(parent: Node?) = DataField(name, type, value, parent)
}

data class Output(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field {
    override fun copy(parent: Node?) = Output(name, type, value, parent)
}

data class OutputReusable(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field {
    override fun copy(parent: Node?) = OutputReusable(name, type, value, parent)
}

data class Input(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field {
    override fun copy(parent: Node?) = Input(name, type, value, parent)
}

data class ConstantDesc(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field {
    override fun copy(parent: Node?) = ConstantDesc(name, type, value, parent)
}