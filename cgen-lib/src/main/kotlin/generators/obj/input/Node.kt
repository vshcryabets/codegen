package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.defs.NotDefined
import generators.obj.out.AstTypeLeaf
import generators.obj.out.CommentLeaf
import generators.obj.out.EnumNode
import generators.obj.out.FileData
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.Separator
import generators.obj.out.VariableName
import kotlin.reflect.KClass


fun <R : Leaf, T : Node> T.findOrNull(clazz: Class<R>): R? {
    subs.forEach {
        if (it.javaClass == clazz) {
            return it as R
        }
    }
    return null
}


fun <R : Leaf, T : Node> T.findOrCreateSub(clazz: Class<R>): R {
    subs.forEach {
        if (it.javaClass == clazz) {
            return it as R
        }
    }

    try {
        // try constructor(name: String, parent: Node)
        val ctor1 = clazz.getConstructor(String::class.java, Node::class.java)
        val newNode = ctor1.newInstance("", this)
        return addSub(newNode)
    } catch (noConstructor: NoSuchMethodException) {
        // try constructor(parent: Node)
        try {
            val ctor2 = clazz.getConstructor(Node::class.java)
            val newNode = ctor2.newInstance(this)
            return addSub(newNode)
        } catch (noConstructor: NoSuchMethodException) {
            // try constructor() without args
            val ctor3 = clazz.getConstructor()
            val newNode = ctor3.newInstance()
            return addSub(newNode)
        }
    }
}


fun <T : Node> T.findParent(kClass: KClass<FileData>): Node? {
    if (kClass.isInstance(this)) {
        return this
    }
    return getParent2()?.findParent(kClass)
}

fun <R : Leaf, T : Node> T.addSub(leaf: R): R {
    subs.add(leaf)
    leaf.setParent2(this)
    (findParent(FileData::class) as FileData?)?.isDirty = true
    return leaf
}

fun <R : Leaf, T : Node> T.addSubs(vararg leafs: R) {
    leafs.forEach {
        subs.add(it)
        it.setParent2(this)
    }
    (findParent(FileData::class) as FileData?)?.isDirty = true
}

//fun <R : Leaf, T : Node> T.addSub2(leaf: R, fnc: R.() -> Unit) {
//    subs.add(leaf)
//    leaf.setParent2(this)
//    (findParent(FileData::class) as FileData?)?.isDirty = true
//    fnc(leaf)
//}

fun <T : Node> T.addOutBlock(name: String = "", function: OutBlock.() -> Unit) =
    addSub(OutBlock(name)).apply(function)

fun <T : Node> T.addOutBlockArguments(name: String = "", function: OutBlockArguments.() -> Unit) =
    addSub(OutBlockArguments(name)).apply(function)


//fun <T : Node> T.addDataField(name: String, dataType: DataType) = addSub(DataField(name, dataType))
//
//fun <T : Node> T.addClassField(name: String, type: DataType, value: DataValue = DataValue.NotDefinedValue) =
//    addSub(DataField(name, type, value))

fun <T : Node> T.addEnumLeaf(name: String) = addSub(EnumNode(name))
fun <T : Node> T.addCommentLine(name: String) = addSub(CommentLeaf(name))
fun <T : Node> T.addSeparator(name: String) = addSub(Separator(name))
fun <T : Node> T.addSeparatorNewLine(name: String = "") = addSub(NlSeparator(name))
fun <T : Node> T.addKeyword(name: String) = addSub(Keyword(name))
fun <T : Node> T.addDatatype(name: String) = addSub(AstTypeLeaf(name))
fun <T : Node> T.addVarName(name: String) = addSub(VariableName(name))
fun <T : Node> T.addRValue(name: String) = addSub(DataValueImpl(name = name, simple = name))
fun <T : Node> T.clearSubs() {
    subs.clear()
}

fun <T : Node> T.removeSub(leaf: Leaf) {
    subs.remove(leaf)
}

fun <T : Leaf> T.copyLeafExt(parent: Node?, fnc: () -> T): T = fnc().also { newCopy ->
    newCopy.setParent2(parent)
}

fun <T : Node> T.copyNodeExt(parent: Node?, copySubs: Boolean, fnc: () -> T): T = fnc().also { newCopy ->
    newCopy.setParent2(parent)
    if (copySubs)
        subs.forEach { newCopy.addSub(it.copyLeaf(this, copySubs)) }
}

interface Node : Leaf {
    val subs: MutableList<Leaf>
    override fun copyLeaf(parent: Node?, copySubs: Boolean ): Node
}

data class Method(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Method = this.copyLeafExt(parent, {this.copy(subs = mutableListOf())})
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class OutputList(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    fun output(name: String, type: DataType) {
        addSub(Output(name).setType(type))
    }

    fun outputReusable(name: String, type: DataType) {
        addSub(OutputReusable(name).setType(type))
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean): OutputList =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

open class Container(
    override val name: String = ""
): Node {
    override val subs: MutableList<Leaf> = mutableListOf()
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Container =
        this.copyLeafExt(parent) { Container(name) }
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

class InputList: Container() {
    fun argument(name: String, type: DataType, value: Any? = NotDefined) {
        addSub(Input(name = name).apply {
            setType(type)
            addSub(DataValueImpl(simple = value))
        })
    }
}

data class InterfaceDescription(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var sourceFile: String = "",
    override var outputFile: String = "",
    override var objectBaseFolder: String = "",
) : Block {
    fun addMethod(name: String, outputs: OutputList? = null, inputs: InputList? = null) {
        addSub(Method(name)).apply {
            outputs?.let { addSub(outputs) }
            inputs?.let { addSub(inputs) }
        }
    }

    override fun addBlockComment(value: String) {
        this.addBlockCommentExt(value)
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean): InterfaceDescription =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}
