package ce.defs

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

// Right value
data class DataValue(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
    val simple: Any? = null,
    val isComplex: Boolean = false,
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node = this.copyLeafExt(parent, {this.copy()})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
    fun notDefined(): Boolean = (simple == NotDefined)
    fun isDefined(): Boolean = simple != NotDefined

    companion object {
        val NotDefinedValue = DataValue(
            name = "NotDefined",
            simple = NotDefined,
            isComplex = false)
    }

    fun leaf(): Leaf = subs.firstOrNull() ?: throw IllegalStateException("No subs in DataValue")
}

object NotDefined
