package generators.cpp

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addSub
import generators.obj.input.copyLeafExt
import generators.obj.input.copyNodeExt
import generators.obj.out.FileData
import generators.obj.out.ImportsBlock

class CompilerDirective(override val name: String) : Leaf {
    var parent: Node? = null
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent) { CompilerDirective(name) }
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class CppHeaderFile(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false
) : FileData {

    init {
        addSub(CompilerDirective("pragma once"))
        addSub(ImportsBlock())
        isDirty = false
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()).apply { subs.clear() }}


    override fun toString(): String = name

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class CppFileData(
    override val name: String = "",
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false
) : FileData {

    init {
        addSub(ImportsBlock())
        isDirty = false
    }

    override fun toString(): String = name

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()).apply { subs.clear() }}

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}
