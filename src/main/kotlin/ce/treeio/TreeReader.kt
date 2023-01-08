package ce.treeio

import generators.obj.input.Leaf

interface TreeReader {
    fun load(filePath: String): Leaf
}