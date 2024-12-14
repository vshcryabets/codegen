package ce.writters

import ce.formatters.CLikeCodestyleRepo
import ce.io.CodeWritter
import ce.settings.CodeStyle
import generators.java.JavaWritter
import generators.obj.input.addOutBlock
import generators.obj.input.addSubs
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.RegionImpl
import generators.obj.out.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaWritterTest {
    @Test
    fun testOutBlock() {
        val codeStyleNoSpace = CodeStyle(
            newLinesBeforeClass = 0,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
        val writter = JavaWritter(repoNoSpace, "")

        val input = RegionImpl().apply {
            addOutBlock("public class TEST") {
                addSubs(
                    Space(),
                    Keyword("{"),
                    Keyword("}"),
                    NlSeparator(),
                )
            }
        }
        var buffer = StringBuffer()
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
        Assert.assertEquals("public class TEST {}\n", buffer.toString())
    }
}