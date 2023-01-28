package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class CommentsBlock() : Node("", null)
class MultilineCommentsBlock() : Node("", null)

class ImportsBlock(name: String, parent : Node) : Node(name, parent) {
    fun addInclude(name: String) {
        addSub(ImportLeaf(name, this))
    }
}

// $name ($OutBlockArguments) {
// ...
// }
open class OutBlock(name: String) : Node(name, null)
open class OutBlockArguments(name: String) : Node(name, null)