package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue

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

    override fun toString(): String = "$name:$type=$value"
    override fun hashCode(): Int = name.hashCode()
}

data class Output(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
    override fun hashCode(): Int = name.hashCode()
}

data class OutputReusable(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
    override fun hashCode(): Int = name.hashCode()
}

data class Input(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
    override fun hashCode(): Int = name.hashCode()
}

data class ConstantDesc(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
    override var parent: Node? = null,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copy(parent = parent)
}