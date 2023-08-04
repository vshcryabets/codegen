package generators.cpp

import generators.obj.input.*
import generators.obj.out.ClassData
import generators.obj.out.FileData

class CompilerDirective(override val name: String, override var parent: Node?) : Leaf {
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = CompilerDirective(name, parent)
}

data class CppClassData(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : ClassData {

    override fun copyLeaf(parent: Node?, copySubs: Boolean): CppClassData =
        this.copyLeafExt(parent, copySubs) { return@copyLeafExt CppClassData(name, parent) }

    override fun toString(): String = name
}

data class CppHeaderFile(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false
) : FileData {

    init {
        addSub(CompilerDirective("pragma once", this))
        isDirty = false
    }

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) { this.copy(parent = parent, subs = mutableListOf()).apply { subs.clear() }}


    override fun toString(): String = name
}

data class CppFileData(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false
) : FileData {

    init {
        addSub(CompilerDirective("pragma once", this))
        isDirty = false
    }

    override fun toString(): String = name

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyLeafExt(parent, copySubs) { this.copy(parent = parent, subs = mutableListOf()).apply { subs.clear() }}
}
