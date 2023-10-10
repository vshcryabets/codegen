package ce.parser

import generators.obj.input.Leaf
import generators.obj.input.Node

class Literal(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Literal(name, parent)
}
class Name(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Name(name, parent)
}
class Word(override val name: String, override var parent: Node? = null, val nextIsLiteral: Boolean = false) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Word(name, parent)
}
class Digit(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = Digit(name, parent)
}