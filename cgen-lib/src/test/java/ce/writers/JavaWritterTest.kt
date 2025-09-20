package ce.writers

import ce.formatters.CLikeCodestyleRepo
import ce.io.CodeWriter
import ce.repository.ReportsRepoImpl
import ce.settings.CodeStyle
import generators.java.JavaWritter
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addSubs
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaWritterTest {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val reportsRepo = ReportsRepoImpl()
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val writter = JavaWritter(repoNoSpace, "",
        reportsRepo = reportsRepo)

    @Test
    fun testOutBlock() {

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
        val buffer = StringBuffer()
        writter.writeNode(input, object : CodeWriter {
            override fun write(str: String): CodeWriter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWriter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWriter = this
            override fun setIndent(str: String): CodeWriter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("public class TEST {}\n", buffer.toString())
    }
}