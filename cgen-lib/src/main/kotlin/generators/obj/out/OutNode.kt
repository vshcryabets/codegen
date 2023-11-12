package generators.obj.out

import generators.obj.input.*

data class CommentsBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }
}

data class MultilineCommentsBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }}

data class ImportsBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Region {

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }

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
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }}

// Outblock Sample (outblock has some prefix, then braces { }
// $OutBlockName ($OutBlockArguments) {
// ...
// }
data class OutBlock(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }

    override fun toString(): String = "OutBlock $name"
}

data class OutBlockArguments(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }}

// TODO think about it
interface ClassData : Region
data class ClassDataImpl(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf>
) : ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) {
            this.copy(subs = mutableListOf(), parent = parent)
        }}