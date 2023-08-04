package generators.obj.out

import generators.obj.input.*

typealias OutNode = Node

data class CommentsBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?): CommentsBlock =
        this.copyLeafExt(parent) { return@copyLeafExt CommentsBlock(name, parent, subs) }
}

data class MultilineCommentsBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?): MultilineCommentsBlock =
        this.copyLeafExt(parent) { return@copyLeafExt MultilineCommentsBlock(name, parent, subs) }
}

data class ImportsBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Region {

    override fun copyLeaf(parent: Node?): ImportsBlock =
        this.copyLeafExt(parent) { return@copyLeafExt ImportsBlock(name, parent, subs) }

    fun addInclude(name: String) {
        addSub(ImportLeaf(name))
    }
}

// Region sample
// // some constants
// const int a = 10;
// const int b = 20;
interface Region : Node

data class RegionImpl(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Region {
    override fun toString() = name
    override fun copyLeaf(parent: Node?): Region =
        this.copyLeafExt(parent) { return@copyLeafExt RegionImpl(name, parent, subs) }
}

// Outblock Sample (outblock has some prefix, then braces { }
// $OutBlockName ($OutBlockArguments) {
// ...
// }
data class OutBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Region {
    override fun copyLeaf(parent: Node?): OutBlock =
        this.copyLeafExt(parent) { return@copyLeafExt OutBlock(name, parent, subs) }
}

data class OutBlockArguments(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?): OutBlockArguments =
        this.copyLeafExt(parent) { return@copyLeafExt OutBlockArguments(name, parent, subs) }
}

// TODO think about it
interface ClassData : Region
data class ClassDataImpl(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf>
) : ClassData {
    override fun copyLeaf(parent: Node?): ClassData =
        this.copyLeafExt(parent) { return@copyLeafExt ClassDataImpl(name, parent, subs) }
}