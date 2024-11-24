package ce.treeio

import generators.obj.input.Node


class TreeMergeWrongRoot: Exception("Tree roots must have same class")
class TreeMergeWrongRootNames(n1: String, n2: String): Exception("Tree root names not equals (${n1} != ${n2})")

object TreeFunctions {
    fun mergeTrees(first: Node, second: Node): Node {
        if (first.javaClass != second.javaClass) {
            throw TreeMergeWrongRoot()
        }
        if (first.name != second.name) {
            throw TreeMergeWrongRootNames(first.name, second.name)
        }
        val result = first.copyLeaf(null, true)
        // find unique root elements
        return result
    }
}