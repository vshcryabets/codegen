package generators.obj.input

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
            value.lines().forEach {
                addSub(CommentLeaf(it.trim()))
            }
        }
    }


}
