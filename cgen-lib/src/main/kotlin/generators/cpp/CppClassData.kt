package generators.cpp

import generators.obj.input.*
import generators.obj.out.ClassData
import generators.obj.out.FileData

class CompilerDirective(override val name: String, override var parent: Node?) : Leaf {
    override fun copyLeaf(parent: Node?) = CompilerDirective(name, parent)
}

data class CppClassData(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : ClassData {
    override fun copyLeaf(parent: Node?): CppClassData =
        this.copyLeafExt(parent) { return@copyLeafExt CppClassData(name, parent) }
}

data class CppScopeGroup(
    override val name: String = "",
    override var parent: Node? = null,
    override val subs: MutableList<Leaf> = mutableListOf()
) : ClassData {
    override fun copyLeaf(parent: Node?): CppScopeGroup =
        this.copyLeafExt(parent) { return@copyLeafExt CppScopeGroup(name, parent) }
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

    override fun getNamespace(name: String): Namespace = getNamespaceExt(name)

    override fun copyLeaf(parent: Node?) =
        this.copyLeafExt(parent) { this.copy(parent = parent, subs = mutableListOf())}
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

    override fun getNamespace(name: String): Namespace = getNamespaceExt(name)

    override fun copyLeaf(parent: Node?): CppFileData =
        this.copyLeafExt(parent) { return@copyLeafExt CppFileData(name, parent) }
}
