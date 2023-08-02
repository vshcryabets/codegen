package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class CommentsBlock() : Node("", null)
class MultilineCommentsBlock() : Node("", null)

class ImportsBlock() : Region("") {
    fun addInclude(name: String) {
        addSub(ImportLeaf(name))
    }
}

// Region sample
// // some constants
// const int a = 10;
// const int b = 20;
open class Region(name: String) : Node(name, null)

// Outblock Sample (outblock has some prefix, then braces { }
// $OutBlockName ($OutBlockArguments) {
// ...
// }
open class OutBlock(name: String) : Region(name)
open class OutBlockArguments(name: String) : Node(name, null)
open class ClassData(name: String) : Region(name) // TODO think about it