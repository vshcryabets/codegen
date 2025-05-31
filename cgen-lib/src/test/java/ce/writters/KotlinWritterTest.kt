package ce.writters

import ce.formatters.CLikeCodestyleRepo
import ce.io.CodeWritter
import ce.settings.CodeStyle
import generators.kotlin.KotlinWritter
import generators.obj.input.addKeyword
import generators.obj.input.addRValue
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.AstTypeLeaf
import generators.obj.out.ConstantNode
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinWritterTest {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val writter = KotlinWritter(repoNoSpace, "")

    @Test
    fun testConstantNodeWithSimpleRvalue() {
        var buffer = StringBuffer()
        val input = ConstantNode().apply {
            addKeyword("const")
            addSeparator(" ")
            addKeyword("val")
            addSeparator(" ")
            addVarName("OREAD")
            addKeyword(":")
            addSeparator(" ")
            addSub(AstTypeLeaf("Int"))
            addSeparator(" ")
            addKeyword("=")
            addSeparator(" ")
            addRValue("0")
        }
        writter.writeNode(input, object : CodeWritter {
            override fun write(str: String): CodeWritter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWritter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWritter = this
            override fun setIndent(str: String): CodeWritter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("const val OREAD: Int = 0", buffer.toString())
    }

}