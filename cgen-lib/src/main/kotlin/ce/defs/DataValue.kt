package ce.defs

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.copyLeafExt

data class RValue(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node = this.copyLeafExt(parent, {this.copy(
        subs = if (copySubs) this.subs else  mutableListOf()
    )})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

// Right value
interface DataValue : Node {
    val simple: Any?
    val isComplex: Boolean
    fun isDefined(): Boolean
}

data class DataValueImpl(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
    override val simple: Any? = null,
    override val isComplex: Boolean = false,
) : DataValue {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
    override fun isDefined(): Boolean = simple != NotDefined

    companion object {
        val NotDefinedValue = DataValueImpl(
            name = "NotDefined",
            simple = NotDefined,
            isComplex = false)
    }
}

data class IntValue(
    override val simple: Long,
    val preferredRadix: Int = 10,
) : DataValue {
    override val isComplex: Boolean = false
    override fun isDefined(): Boolean = true
    override val subs: MutableList<Leaf> = mutableListOf()

    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node = this.copyLeafExt(parent, {this.copy()})

    override val name: String = ""
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent } }

object NotDefined
