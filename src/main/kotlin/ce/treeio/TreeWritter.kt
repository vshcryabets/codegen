package ce.treeio

import generators.obj.input.Leaf
import generators.obj.input.Namespace

interface TreeWritter {
    fun storeTree(filePath: String, tree: Leaf)
}