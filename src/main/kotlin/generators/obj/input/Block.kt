package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefinedValue
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock

open class Block(
    name: String,
    parent: Node
) : Node(name, parent) {
    var sourceFile = ""
    var outputFile = ""
    var objectBaseFolder = ""

    fun addBlockComment(value : String) {
        findOrCreateSub(CommentsBlock::class.java).apply {
            addSub(CommentLeaf(value))
        }
    }


}
