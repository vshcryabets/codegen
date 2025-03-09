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
    val static: Boolean = false,
) : Field {

    override fun toString(): String = "$name:$type=$value"
    override fun hashCode(): Int = name.hashCode()

    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }

}

data class Output(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class OutputReusable(
    override val name: String,
    override val type: DataType,
    override var value: DataValue = NotDefinedValue,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class Input(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class ConstantDesc(
    override val name: String,
    override val type: DataType,
    override var value: DataValue,
) : Field {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }

}