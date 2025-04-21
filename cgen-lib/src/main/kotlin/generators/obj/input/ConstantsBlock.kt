package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.defs.IntValue
import ce.defs.NotDefined

data class ConstantsBlock(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
    var defaultDataType: DataType = DataType.VOID,
    var preferredRadix: Int = 10,
) : Block {
    override fun toString() = name

    fun preferredRadix(radix: Int) {
        preferredRadix = radix
    }

    fun defaultType(name: DataType) {
        defaultDataType = name
    }

    fun add(name: String, value: Any? = NotDefined) : ConstantDesc = add(name, defaultDataType, value)

    fun add(name: String, type : DataType, value: Any? = NotDefined): ConstantDesc {
        val dv = if (type in DataType.INTEGERS) {
            IntValue(value.toString().toLong(), preferredRadix = preferredRadix)
        } else {
            DataValueImpl(simple = value)
        }
        return addSub(ConstantDesc(name).apply {
            setType(type)
            setValue(dv)
        })
    }

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