package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined

data class DataClass(
    override val name: String,
    override var parent: Node?,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
) : Block {

    fun field(name: String, type: DataType, value: Any? = NotDefined) {
        addSub(DataField(name, type, DataValue(value)))
    }

    override fun addBlockComment(value: String) {
        this.addBlockCommentExt(value)
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }
}
