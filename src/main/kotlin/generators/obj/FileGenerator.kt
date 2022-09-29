package generators.obj

import ce.settings.CodeStyle
import generators.obj.input.Block
import generators.obj.out.CommentLeaf
import generators.obj.out.FileData
import generators.obj.out.CommentsBlock
import generators.obj.out.ProjectOutput

abstract class FileGenerator(val style : CodeStyle) {
    val tabSpace : String

    init {
        val builder = StringBuilder()
        for (a in 0..style.tabSize - 1) {
            builder.append(" ");
        }
        tabSpace = builder.toString()
    }

    abstract fun createFile(project: ProjectOutput, outputFile: String, block: Block): List<FileData>
    abstract fun multilineCommentStart() : String
    abstract fun multilineCommentMid() : String
    abstract fun multilineCommentEnd() : String
    abstract fun singleComment() : String
    abstract fun newLine() : String

    abstract fun getBlockFilePath(block: Block): String

    open fun appendInitialComment(s: FileData, s1: String) {
        val commentsNode = s.findSub(CommentsBlock::class.java)
        if (s1.trimIndent().isNotEmpty()) {
            s1.lines().forEach { line ->
                commentsNode.subs.add(CommentLeaf("${singleComment()} $line", commentsNode))
            }
        }
    }

}