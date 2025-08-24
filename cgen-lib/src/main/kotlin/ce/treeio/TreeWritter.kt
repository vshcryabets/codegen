package ce.treeio

import generators.obj.abstractSyntaxTree.Leaf

interface TreeWritter {
    fun storeTree(filePath: String, tree: Leaf)
}