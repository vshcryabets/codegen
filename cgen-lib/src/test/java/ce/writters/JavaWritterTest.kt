package ce.writters

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.io.CodeWritter
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.java.JavaConstantsGenerator
import generators.java.JavaWritter
import generators.obj.input.ConstantsBlock
import generators.obj.input.NamespaceImpl
import generators.obj.input.addOutBlock
import generators.obj.input.addSubs
import generators.obj.out.ConstantNode
import generators.obj.out.Datatype
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.OutputTree
import generators.obj.out.RegionImpl
import generators.obj.out.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaWritterTest {
    val reader = XmlTreeReader()
    val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repo = CLikeCodestyleRepo(codeStyle)
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val writter = JavaWritter(repoNoSpace, "")
    val buffer = StringBuilder()
    val codeWritter = object : CodeWritter {
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
    }

    @Test
    fun testOutBlock() {
        buffer.clear()
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
        writter.writeNode(input, codeWritter, "")
        Assert.assertEquals("public class TEST {}\n", buffer.toString())
    }

}