package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class CommentsBlock(parent : Node) : Node("", parent)
class MultilineCommentsBlock() : Node("", null)

class ImportsBlock(name: String, parent : Node) : Node(name, parent) {
    fun addInclude(name: String) {
        addSub(ImportLeaf(name, this))
    }
}

// $name ($OutBlockArguments) {
// ...
// }
open class OutBlock(name: String, parent: Node) : Node(name, parent)
open class OutBlockArguments(name: String, parent: Node) : Node(name, parent)