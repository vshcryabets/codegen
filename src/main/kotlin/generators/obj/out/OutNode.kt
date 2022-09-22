package generators.obj.out

import generators.obj.input.Node

typealias OutNode = Node

class FileInitialCommentsBlock(parent : OutNode) : OutNode("", parent)
open class OutNamespace(name: String, parent: OutNode) : OutNode(name, parent)

class ImportsBlock(parent : OutNode) : OutNode("", parent) {

    fun addInclude(name: String) {
        subs.add(ImportLeaf(name, this))
    }
}