package ce.defs

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

data class DataValue(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
    val simple: Any?,
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
}

object NotDefined
