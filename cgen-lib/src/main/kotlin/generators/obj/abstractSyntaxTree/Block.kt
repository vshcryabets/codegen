package generators.obj.abstractSyntaxTree

import generators.obj.syntaxParseTree.CommentLeaf
import generators.obj.syntaxParseTree.CommentsBlock

fun <T: Block> T.addBlockCommentExt(value : String) {
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

    // Used in KTS
    fun addBlockComment(value : String)
}
