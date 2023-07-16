package generators.obj.input

import ce.defs.*
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import generators.obj.out.*

object TreeRoot : Node("ROOT", null)

open class Leaf(val name: String,
                @JsonBackReference
                var parent: Node? = null) {
    @JsonIgnore
    fun getParentPath(): String = parent?.getPath() ?: ""

    @JsonIgnore
    fun getPath(): String {
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
}

open class Node(name: String,
                parent: Node?,
                @JsonManagedReference
                val subs: MutableList<Leaf> = mutableListOf()) :
    Leaf(name, parent) {

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

    fun <T : Leaf> addSub(leaf: T): T {
        subs.add(leaf)
        leaf.parent = this
        return leaf
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
    fun addVarName(name: String) = addSub(VariableName(name))

    fun addRValue(name: String) = addSub(RValue(name))
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