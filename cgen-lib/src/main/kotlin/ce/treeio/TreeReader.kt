package ce.treeio

import generators.obj.input.Leaf

/**
 * Interface for reading a tree structure from a file or a string.
 */
interface TreeReader {
    /**
     * Loads the tree structure from the specified file path.
     *
     * @param filePath The path of the file from which the tree will be loaded.
     * @return The loaded tree structure as a Leaf object.
     */
    fun load(filePath: String): Leaf

    /**
     * Loads the tree structure from the given string data.
     *
     * @param data The string data from which the tree will be loaded.
     * @return The loaded tree structure as a Leaf object.
     */
    fun loadFromString(data: String): Leaf
}