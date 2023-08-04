package generators.obj.out

import ce.parsing.Literal
import generators.obj.input.Leaf
import generators.obj.input.Node

// package $name
class NamespaceDeclaration(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = NamespaceDeclaration(name, parent)
}

class NamespaceBlock(name: String) : Node(name, null)

//import $name
class ImportLeaf(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = ImportLeaf(name, parent)
}

// //$name
class CommentLeaf(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = CommentLeaf(name, parent)
}

class ArgumentLeaf(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = ArgumentLeaf(name, parent)
}

class ResultLeaf(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = ResultLeaf(name, parent)
}

class FieldLeaf(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = FieldLeaf(name, parent)
}

class ConstantLeaf(fnc: ConstantLeaf.() -> Unit) : Node("", null) {
    init {
        fnc(this)
    }
}

class EnumLeaf(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = EnumLeaf(name, parent)
}

// something after "="
class RValue(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = RValue(name, parent)
}

class Keyword(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = Keyword(name, parent)
}
class Datatype(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = Datatype(name, parent)
}
class VariableName(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = VariableName(name, parent)
}

// for example ",\n"
class Separator(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = Separator(name, parent)
}
class NlSeparator(override val name: String = "", override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = NlSeparator(name, parent)
}
@Deprecated("Shouldn't be used")
class BlockPreNewLines(override val name: String = "", override var parent: Node? = null) : Leaf {
    override fun copy(parent: Node?) = BlockPreNewLines(name, parent)
}