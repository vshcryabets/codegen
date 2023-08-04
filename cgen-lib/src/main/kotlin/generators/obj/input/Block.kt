package generators.obj.input

import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock

// Used in KTS
fun <T: Block> T.addBlockComment(value : String) {
    findOrCreateSub(CommentsBlock::class.java).apply {
        value.lines().forEach {
            addSub(CommentLeaf(it.trim()))
        }
    }
}
interface Block: Node {
    var sourceFile: String
    var outputFile: String
    var objectBaseFolder: String
}
