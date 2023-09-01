package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue
import generators.cpp.CompilerDirective


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
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
}

data class Output(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
}

data class OutputReusable(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
}

data class Input(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
}

data class ConstantDesc(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
}