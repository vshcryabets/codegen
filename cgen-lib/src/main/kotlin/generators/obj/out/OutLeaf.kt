package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.copyLeafExt

// package $name
data class NamespaceBlock(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }

    override fun toString() = name
}

//import $name
data class ImportLeaf(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }

}

// //$name
data class CommentLeaf(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class ResultLeaf(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class FieldNode(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean): FieldNode =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class EnumNode(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean): EnumNode =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class ArgumentNode(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Node =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class Keyword(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class AstTypeLeaf(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class VariableName(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

// for example "," or ";"
data class Separator(
    override val name: String,
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class NlSeparator(
    override val name: String = "",
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class Indent(
    override val name: String = "",
) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class Space(
    override val name: String = "",
) : Leaf {
    override fun toString() = "< >"
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf =
        this.copyLeafExt(parent, { this.copy() })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}
