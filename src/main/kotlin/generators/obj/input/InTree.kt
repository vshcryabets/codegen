package generators.obj.input

import ce.defs.DataType

object TreeRoot : Node("ROOT", null)

open class Leaf(val name: String, val parent: Node?) {
    fun getParentPath(): String = parent?.getPath() ?: ""
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

open class Node(name: String, parent: Node?, val subs: MutableList<Leaf> = mutableListOf()) :
    Leaf(name, parent) {
    fun <T : Node> findSub(clazz: Class<T>): T {
        subs.forEach {
            if (it.javaClass == clazz) {
                return it as T
            }
        }
        val newNode = clazz.getConstructor(String::class.java, Node::class.java).newInstance("", this)
        return addSub(newNode)
    }

    fun <T : Leaf> addSub(leaf: T): T {
        subs.add(leaf)
        return leaf
    }
}

open class Namespace(name: String, parent: Node) : Node(name, parent) {
    fun getNamespace(name: String): Namespace {
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
        val newNamaspace = Namespace(searchName, this)
        subs.add(newNamaspace)
        return newNamaspace.getNamespace(endPath)
    }
}

open class Method(name: String, parent: Node) : Node(name, parent)
open class OutputList() : Node("", null) {
    fun output(name: String, type : DataType) {
        subs.add(ClassField(name, this, type, NotDefined))
    }

    fun outputReusable(name: String, type : DataType) {
        subs.add(ClassField(name, this, type, NotDefined))
    }
}
open class InputList() : Node("", null) {
    fun argument(name: String, type : DataType, value: Any? = NotDefined) {
        subs.add(ClassField(name, this, type, value))
    }
}

class InterfaceDescription(name: String, parent: Node) : Block(name, parent) {
    fun addMethod(name: String, outputs: OutputList?, inputs: InputList?) {
        addSub(Method(name, this)).apply {
            outputs?.let { addSub(outputs) }
            inputs?.let { addSub(inputs) }
        }
    }
}