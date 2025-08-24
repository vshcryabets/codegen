package ce.formatters.java

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterJavaUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addRValue
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.Indent
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaConstantsFormattingTests {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val formatter = CodeFormatterJavaUseCaseImpl(repoNoSpace)

    @Test
    fun testConstantsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("public class TEST") {
                addSub(FieldNode().apply {
                    addKeyword("public")
                    addKeyword("static")
                    addKeyword("final")
                    addDatatype("int")
                    addVarName("OREAD")
                    addKeyword("=")
                    addRValue("0")
                })
                addSub(FieldNode().apply {
                    addKeyword("public")
                    addKeyword("static")
                    addKeyword("final")
                    addDatatype("int")
                    addVarName("OWRITE")
                    addKeyword("=")
                    addRValue("1")
                })
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SP> <{> <nl>
        //        <TAB><ConstantNode>
        //            <public><SP><static><SP><final><SP><int><SP><OREAD><SP><=><SP><0>
        //        </ConstantNode>
        //        <;><NL>
        //        <TAB><ConstantNode>
        //            <public><SP><static><SP><final><SP><int><SP><OWRITE><SP><=><SP><1>
        //        </ConstantNode>
        //        <;><NL>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(12, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Space)
        Assert.assertTrue(outBlock.subs[1] is Keyword)
        Assert.assertTrue(outBlock.subs[2] is NlSeparator)
        Assert.assertTrue(outBlock.subs[3] is Indent)
        Assert.assertTrue(outBlock.subs[4] is FieldNode)

        val constantNode = outBlock.subs[4] as FieldNode
        Assert.assertEquals(13, constantNode.subs.size)
    }

}