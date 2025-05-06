package ce.parser.domain.dictionaries.tree

import generators.obj.input.Leaf
import generators.obj.out.Keyword
import generators.obj.out.VariableName

/**
 * Data class representing the data of a tree node.
 *
 * @property openId The ID when the node is opened.
 * @property closeId The ID when the node is closed. Default is -1.
 * @property priority The priority of the node. Default is 0.
 */
data class TreeNodeData(
    val openId: Int,
    val closeId: Int = -1,
    val priority: Int = 0,
) {
    companion object {
        /**
         * Checks if the given leaf is unique.
         *
         * @param leaf The leaf to check.
         * @return True if the leaf is a VariableName or Keyword, false otherwise.
         */
        fun isUniq(leaf: Leaf): Boolean {
            return when (leaf) {
                is VariableName -> true
                is Keyword -> true
                else -> false
            }
        }

        /**
         * Converts the given leaf to an ID string.
         *
         * @param leaf The leaf to convert.
         * @return The ID string of the leaf. If the leaf is unique, the ID includes the leaf's name.
         */
        fun toId(leaf: Leaf): String {
            if (isUniq(leaf)) {
                return leaf.javaClass.simpleName + "_" + leaf.name
            }
            return leaf.javaClass.simpleName
        }
    }
}