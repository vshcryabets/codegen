package ce.treeio

import generators.obj.abstractSyntaxTree.Leaf

interface TreeReader {
    fun load(filePath: String): Leaf
    fun loadFromString(data: String): Leaf
}