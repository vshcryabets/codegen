package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class CommentsBlock() : Node("", null)
class MultilineCommentsBlock() : Node("", null)

class ImportsBlock() : Node("", null) {
    fun addInclude(name: String) {
        addSub(ImportLeaf(name))
    }
}

open class Region(name: String) : Node(name, null)

// $OutBlockName ($OutBlockArguments) {
// ...
// }
open class OutBlock(name: String) : Region(name)
open class OutBlockArguments(name: String) : Node(name, null)
open class ClassData(name: String) : Region(name)