package generators.obj

import generators.obj.input.Block
import generators.obj.input.Node
import generators.obj.out.*

abstract class Generator<I: Block, T : ClassData >(val fileGenerator: FileGenerator) {

    abstract fun processBlock(file: FileData, parent: Node, desc: I) : T

    fun putTabs(builder: StringBuilder, count : Int) {
        for (i in 0 .. count - 1) {
            builder.append(fileGenerator.tabSpace)
        }
    }

    fun appendNotEmptyWithNewLine(str: StringBuilder, builder: StringBuilder) {
        appendNotEmptyWithNewLine(str.toString(), builder)
    }
    fun appendNotEmptyWithNewLine(str: String, builder: StringBuilder) {
        if (str.isNotEmpty()) {
            builder.append(str).append(fileGenerator.newLine())
        }
    }

    fun appendClassDefinition(outputClassData: ClassData, s: String) {
        outputClassData.apply {
            classDefinition.append(s)
            classDefinition.append(fileGenerator.newLine())
        }
    }

    protected fun addBlockDefaults(desc: Block, result: ClassData) {
        result.subs.add(BlockPreNewLines(result))
        addMultilineCommentsBlock(desc.classComment.toString(), result)
    }

    fun addMultilineCommentsBlock(comment: String, parent: Node) {
        if (comment.isNotEmpty()) {
            MultilineCommentsBlock(parent).apply {
                comment.lines().forEach { line ->
                    subs.add(CommentLeaf("${fileGenerator.multilineCommentMid()} $line", this))
                }
                parent.subs.add(this)
            }
        }
    }
}