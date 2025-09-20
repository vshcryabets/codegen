package generators.obj.syntaxParseTree

import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.copyLeafExt

interface FileData: Node {
    var isDirty: Boolean
}

data class FileMetaInformation(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
): Node {
    var parent: Node? = null
    override fun copyLeaf(parent: Node?, copySubs: Boolean): FileMetaInformation =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class WorkingDirectory(
    override val name: String,
): Leaf {
    var parent: Node? = null
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent) { WorkingDirectory(name) }
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}

data class PackageDirectory(
    override val name: String,
): Leaf {
    var parent: Node? = null
    override fun copyLeaf(parent: Node?, copySubs: Boolean): Leaf = this.copyLeafExt(parent) { PackageDirectory(name) }
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}


data class FileDataImpl(
    override val name: String,
    override val subs: MutableList<Leaf> = mutableListOf(),
    override var isDirty: Boolean = false,
) : FileData {

    override fun toString(): String = name

    override fun copyLeaf(parent: Node?, copySubs: Boolean): FileData =
        this.copyLeafExt(parent, { this.copy(subs = mutableListOf()) })
    var parent: Node? = null
    override fun getParent2(): Node? = parent
    override fun setParent2(parent: Node?) { this.parent = parent }
}