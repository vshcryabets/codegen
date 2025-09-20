package generators.cpp

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.copyLeafExt
import generators.obj.abstractSyntaxTree.copyNodeExt
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.ImportsBlock

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

    override fun toString(): String = name

    override fun copyLeaf(parent: Node?, copySubs: Boolean) =
        this.copyNodeExt(parent, copySubs) { this.copy(subs = mutableListOf()).apply { subs.clear() }}

    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}
