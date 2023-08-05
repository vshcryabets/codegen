package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

// package $name
data class NamespaceDeclaration(override val name: String, override var parent: Node? = null) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class NamespaceBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }

    override fun toString() = name
}

//import $name
data class ImportLeaf(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

// //$name
data class CommentLeaf(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class ArgumentLeaf(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class ResultLeaf(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class FieldLeaf(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class ConstantLeaf(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }
}

data class EnumLeaf(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

// something after "="
data class RValue(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class Keyword(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class Datatype(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class VariableName(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

// for example ",\n"
data class Separator(
    override val name: String,
    override var parent: Node? = null
) : Leaf {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class NlSeparator(
    override val name: String = "",
    override var parent: Node? = null
) : Leaf {
    override fun toString() = "<NL>"
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class Indent(
    override val name: String = "",
    override var parent: Node? = null
) : Leaf {
    override fun toString() = "<TAB>"
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}

data class Space(
    override val name: String = "",
    override var parent: Node? = null
) : Leaf {
    override fun toString() = "< >"
    override fun copyLeaf(parent: Node?, copySubs: Boolean) = this.copy(parent = parent)
}
