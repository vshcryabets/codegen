package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined
import ce.defs.NotDefinedValue
import generators.obj.out.*
import kotlin.reflect.KClass

object TreeRoot : Node("ROOT", null)


fun <T: Leaf> T.getParentPath(): String = parent?.getPath() ?: ""

fun <T: Leaf> T.getPath(): String {
    if (parent == null) {
        return ""
    }
    val parentPath = getParentPath()
    return if (parentPath.isNotEmpty()) {
        "$parentPath.$name"
    } else {
        name
    }

}

interface Leaf {
    val name: String
    var parent: Node?
}

open class Node(
    override val name: String,
    override var parent: Node?) :
    Leaf {

    private val _subs: MutableList<Leaf> = mutableListOf()
    val subs: List<Leaf> = _subs

    fun <T : Leaf> findOrNull(clazz: Class<T>): T? {
        subs.forEach {
            if (it.javaClass == clazz) {
                return it as T
            }
        }
        return null
    }

    fun <T : Node> findOrCreateSub(clazz: Class<T>): T {
        subs.forEach {
            if (it.javaClass == clazz) {
                return it as T
            }
        }

        try {
            // try contructor(name: String, parent: Node)
            val ctor1 = clazz.getConstructor(String::class.java, Node::class.java)
            val newNode = ctor1.newInstance("", this)
            return addSub(newNode)
        } catch (noConstructor : NoSuchMethodException) {
            // try contructor(parent: Node)
            try {
                val ctor2 = clazz.getConstructor(Node::class.java)
                val newNode = ctor2.newInstance(this)
                return addSub(newNode)
            } catch (noConstructor : NoSuchMethodException) {
                // try contructor() without args
                val ctor3 = clazz.getConstructor()
                val newNode = ctor3.newInstance()
                return addSub(newNode)
            }
        }
    }

    fun findParent(kClass: KClass<FileData>): Node? {
        if (kClass.isInstance(this)) {
            return this
        }
        return parent?.findParent(kClass)
    }

    fun <T : Leaf> addSub(leaf: T): T {
        _subs.add(leaf)
        leaf.parent = this
        (findParent(FileData::class) as FileData?)?.setDirtyFlag()
        return leaf
    }

    fun <T : Leaf> addSub2(leaf: T, fnc: T.()->Unit) {
        _subs.add(leaf)
        leaf.parent = this
        (findParent(FileData::class) as FileData?)?.setDirtyFlag()
        fnc(leaf)
    }

    fun addOutBlock(name: String = "", function: OutBlock.() -> Unit) =
        addSub(OutBlock(name)).apply(function)

    fun addOutBlockArguments(name: String = "", function: OutBlockArguments.() -> Unit) =
        addSub(OutBlockArguments(name)).apply(function)


    fun addDataField(name: String, dataType: DataType) = addSub(DataField(name, dataType))

    fun addClassField(name: String, type : DataType, value: DataValue = NotDefinedValue) =
        addSub(DataField(name, type, value))

    fun addEnumLeaf(name: String) = addSub(EnumLeaf(name))

    fun addCommentLine(name: String) = addSub(CommentLeaf(name))

    fun addSeparator(name: String) = addSub(Separator(name))
    fun addSeparatorNewLine(name: String) = addSub(NlSeparator(name))
    fun addKeyword(name: String) = addSub(Keyword(name))
    fun addDatatype(name: String) = addSub(Datatype(name))
    fun addVarName(name: String) = addSub(VariableName(name))

    fun addRValue(name: String) = addSub(RValue(name))
    fun clearSubs() {
        _subs.clear()
    }

    fun removeSub(leaf: Leaf) {
        _subs.remove(leaf)
    }
}

open class Method(name: String) : Node(name, null)

open class OutputList() : Node("", null) {
    fun output(name: String, type : DataType) {
        addSub(Output(name, type))
    }

    fun outputReusable(name: String, type : DataType) {
        addSub(OutputReusable(name, type))
    }
}
open class InputList() : Node("", null) {
    fun argument(name: String, type : DataType, value: Any? = NotDefined) {
        addSub(Input(name = name, type = type, value = DataValue(value)))
    }
}

class InterfaceDescription(name: String, parent: Node) : Block(name, parent) {
    fun addMethod(name: String, outputs: OutputList? = null, inputs: InputList? = null) {
        addSub(Method(name)).apply {
            outputs?.let { addSub(outputs) }
            inputs?.let { addSub(inputs) }
        }
    }
}
