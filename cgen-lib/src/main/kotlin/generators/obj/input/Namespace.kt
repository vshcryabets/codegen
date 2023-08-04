package generators.obj.input

import ce.defs.customBaseFolderPath
import ce.defs.outputFile
import ce.defs.sourceFile
import generators.obj.out.OutBlock

fun <T:Namespace> T.getNamespaceExt(name: String): Namespace {
    if (name.isEmpty()) {
        return this
    }
    val pointPos = name.indexOf('.')
    val searchName: String
    val endPath: String
    if (pointPos < 0) {
        searchName = name
        endPath = ""
    } else {
        searchName = name.substring(0, pointPos)
        endPath = name.substring(pointPos + 1)
    }

    subs.forEach {
        if (it is Namespace) {
            if (it.name == searchName) {
                return it.getNamespace(endPath)
            }
        }
    }
    return addSub(NamespaceImpl(searchName, this)).getNamespace(endPath)
}
interface Namespace: Node {
    fun getNamespace(name: String): Namespace
}
data class NamespaceImpl(override val name: String = "",
                         override var parent: Node? = null,
                         override val subs: MutableList<Leaf> = mutableListOf()
) : Namespace {
    override fun toString() = name

    fun putDefaults(block: Block) {
        block.objectBaseFolder = customBaseFolderPath
        block.sourceFile = sourceFile
        block.outputFile = if (outputFile.isEmpty()) block.name else outputFile
        println("Block ${block.name} = ${block.outputFile}");
    }

    fun enum(name: String): ConstantsEnum {
        return ConstantsEnum(name, this).apply {
            putDefaults(this)
        }
    }

    fun constantsBlock(name: String): ConstantsBlock {
        return addSub(ConstantsBlock(name)).apply {
            putDefaults(this)
        }
    }

    fun dataClass(name: String): DataClass {
        return DataClass(name, this)
            .apply {
                putDefaults(this)
            }
    }

    fun declareInterface(name: String): InterfaceDescription {
        return InterfaceDescription(name, this)
            .apply {
                putDefaults(this)
            }
    }

    override fun getNamespace(name: String): Namespace = getNamespaceExt(name)

    override fun copyLeaf(parent: Node?): Namespace = this.copyLeafExt(parent) {return@copyLeafExt NamespaceImpl(name, parent) }
}
