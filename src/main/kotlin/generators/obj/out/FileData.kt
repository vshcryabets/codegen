package generators.obj.out

import generators.obj.input.Namespace
import generators.obj.input.Node

open class FileData(name: String, parent: Node) : Namespace(name, parent) {
    init {
        subs.add(CommentsBlock(this))
    }
}