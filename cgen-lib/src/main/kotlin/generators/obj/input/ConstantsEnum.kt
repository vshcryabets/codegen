package generators.obj.input

import ce.defs.DataType
import ce.defs.NotDefined

data class ConstantsEnum(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
    var defaultDataType: DataType = DataType.VOID
) : Block {
    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String) = add(name, NotDefined)

    fun add(name: String, value: Any) {
        addSub(DataField(name).apply {
            setType(defaultDataType)
            setValue(value)
        })
    }

    override fun toString(): String = "<ConstantsEnum $name>"

    override fun addBlockComment(value: String) {
        this.addBlockCommentExt(value)
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) {
            this.copy(subs = mutableListOf())
        }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}
