package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.NotDefined

interface Field : Node {
    fun getValue(): DataValue
    fun getType(): DataType
    fun setType(type: DataType): Field
    fun setValue(value: Any?): Field
}

open class FieldImpl(name: String, type: DataType? = null) : Container(name), Field {
    init {
        type?.let {
            addSub(TypeLeaf(type = it))
        }
    }
    override fun getType(): DataType {
        return subs.filterIsInstance<TypeLeaf>().lastOrNull()?.type ?: DataType.Unknown
    }
    fun getTypeOrNull(): DataType? {
        return subs.filterIsInstance<TypeLeaf>().lastOrNull()?.type
    }
    override fun getValue(): DataValue = subs.filterIsInstance<DataValue>().lastOrNull() ?: DataValueImpl.NotDefinedValue

    override fun setValue(value: Any?): FieldImpl {
        subs.removeAll { it is DataValue }
        subs.add(when (value) {
            is NotDefined -> DataValueImpl.NotDefinedValue
            is DataValue -> value
            is Node -> DataValueImpl(simple = false, isComplex = true).apply { addSub(value) }
            else -> DataValueImpl(simple = value)
        })
        return this
    }

    override fun setType(type: DataType): FieldImpl {
        subs.removeAll { it is TypeLeaf }
        addSub(TypeLeaf(type = type))
        return this
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

open class DataField(
    name: String,
    val static: Boolean = false,
): FieldImpl(
    name = name
) {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Container =
        this.copyLeafExt(parent, { DataField(
            name = this.name,
            static = this.static
        ) })
}

class Output(name: String): FieldImpl(name)
class OutputReusable(name: String): FieldImpl(name)
class Input(name: String): FieldImpl(name)
class ConstantDesc(name: String): FieldImpl(name)

