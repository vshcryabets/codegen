package ce.parser

import generators.obj.input.Leaf
import generators.obj.input.Node

class Literal(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Literal(name, parent)
    override fun toString(): String = name
}

class Name(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Name(name, parent)
    override fun toString(): String = name
}

data class Word(
    val name: String,
    val nextIsLiteral: Boolean = false,
    val id: Int = -1,
    )  {
//    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Word(name, parent, nextIsLiteral, id)
    override fun toString(): String = name
}

class Digit(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Digit(name, parent)
    override fun toString(): String = name
}