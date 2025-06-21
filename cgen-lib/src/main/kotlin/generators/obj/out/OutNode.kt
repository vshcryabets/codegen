package generators.obj.out

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSub
import generators.obj.input.copyNodeExt

data class CommentsBlock(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean): CommentsBlock =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

// #include <iostream>
// #include <string>
// #include <vector>
data class ImportsBlock(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Region {

    override fun copyLeaf(parent: Node?, copySubs: Boolean): ImportsBlock =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }

    fun addInclude(name: String) {
        if (subs.any { it is ImportLeaf && it.name == name }) return
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
    override val subs: MutableList<Leaf> = mutableListOf()
) : Region {
    override fun toString() = name
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Region =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

// Outblock Sample (outblock has some prefix, then braces { }
// $OutBlockName ($OutBlockArguments) {
// ...
// }
data class OutBlock(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): OutBlock =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class OutBlockArguments(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): OutBlockArguments =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

// TODO think about it
interface ClassData : Region
data class ClassDataImpl(
    override val name: String = "",
    override val subs: MutableList<Leaf>
) : ClassData {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): ClassData =
        this.copyNodeExt(parent, copySubs, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

// new MyClass(arguments...)
data class Constructor(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Constructor =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class Arguments(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf()
) : Node {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Arguments =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()) }

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}