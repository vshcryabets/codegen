package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.NotDefined

interface Field : Node {
    fun getValue(): DataValue
    fun getType(): DataType
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

fun <T : Field> T.setValue(value: Any?): T {
    subs.removeAll { it is DataValue }
    subs.add(when (value) {
        is NotDefined -> DataValueImpl.NotDefinedValue
        is DataValue -> value
        is Node -> DataValueImpl(simple = false, isComplex = true).apply { addSub(value) }
        else -> DataValueImpl(simple = value)
    })
    return this
}

fun <T : Field> T.setType(type: DataType): T {
    subs.removeAll { it is DataType }
    addSub(TypeLeaf(type = type))
    return this
}
