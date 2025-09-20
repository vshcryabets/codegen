package ce.treeio

import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.addSub


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
        second.subs.forEach { secondIt ->
            val sameFirstIdx = result.subs.indexOfFirst {
                it.name == secondIt.name && it.javaClass == secondIt.javaClass
            }
            if (sameFirstIdx < 0) {
                result.addSub(secondIt)
            } else {
                val sameFirst = result.subs[sameFirstIdx]
                if (sameFirst !is Node || secondIt !is Node) {
                    throw IllegalStateException("$sameFirst or $secondIt is not Node")
                } else {
                    val update = mergeTrees(sameFirst, secondIt)
                    result.subs[sameFirstIdx] = update
                }
            }
        }
        return result
    }
}