package ce.treeio

import generators.obj.input.Leaf

/**
 * Interface for writing a tree structure to a file.
 */
interface TreeWritter {
    /**
     * Stores the given tree structure to the specified file path.
     *
     * @param filePath The path of the file where the tree will be stored.
     * @param tree The tree structure to store.
     */
    fun storeTree(filePath: String, tree: Leaf)
}