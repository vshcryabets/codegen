package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.NotDefined

interface Field : Node
open class FieldImpl(name: String, type: DataType? = null) : Container(name), Field {
    init {
        type?.let {
            addSub(TypeLeaf(type = it))
        }
    }
    fun getType(): DataType {
        return subs.filterIsInstance<TypeLeaf>().firstOrNull()?.type ?: DataType.Unknown
    }
    fun getTypeOrNull(): DataType? {
        return subs.filterIsInstance<TypeLeaf>().firstOrNull()?.type
    }
}

data class TypeLeaf(
    override val name: String = "",
    val type: DataType
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): TypeLeaf = this.copyLeafExt(parent) { this.copy() }
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

class ModifiersList: Container()

data class DataField(
    override val name: String,
    val dataType: DataType? = null,
    val static: Boolean = false,
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Field {
    init {
        dataType?.let {
            addSub(TypeLeaf(type = it))
        }
    }

    fun getType(): DataType {
        return subs.filterIsInstance<TypeLeaf>().firstOrNull()?.type ?: DataType.Unknown
    }

    override fun toString(): String = "$name:${getType()}=${subs.firstOrNull()?.toString() ?: "null"}"
    override fun hashCode(): Int = name.hashCode()

    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

class Output(name: String): FieldImpl(name)
class OutputReusable(name: String): FieldImpl(name)
class Input(name: String): FieldImpl(name)
class ConstantDesc(name: String): FieldImpl(name)

fun <T : Field> T.setValue(value: Any?): T {
    subs.clear()
    subs.add(when (value) {
        is NotDefined -> DataValueImpl.NotDefinedValue
        is DataValue -> value
        is Node -> DataValueImpl(simple = false, isComplex = true).apply { addSub(value) }
        else -> DataValueImpl(simple = value)
    })
    return this
}

fun <T : Field> T.getValue(): DataValue = subs.filterIsInstance<DataValue>().firstOrNull() ?: DataValueImpl.NotDefinedValue
fun <T : Field> T.setType(type: DataType) = addSub(TypeLeaf(type = type))
