package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class CommentsBlock(parent : Node) : Node("", parent)
class MultilineCommentsBlock(parent : Node) : Node("", parent)

class ImportsBlock(name: String, parent : Node) : Node(name, parent) {
    fun addInclude(name: String) {
        subs.add(ImportLeaf(name, this))
    }
}

open class OutBlock(name: String, parent: Node) : Node(name, parent)