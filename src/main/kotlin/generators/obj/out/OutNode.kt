package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class CommentsBlock(parent : Node) : Node("", parent)
class MultilineCommentsBlock(parent : Node) : Node("", parent)
open class OutNamespace(name: String, parent: OutNode) : OutNode(name, parent)

class ImportsBlock(parent : OutNode) : OutNode("", parent) {

    fun addInclude(name: String) {
        subs.add(ImportLeaf(name, this))
    }
}