package ce.formatters

import ce.settings.CodeStyle
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterKotlinObjectUseCaseImplTest {
    val codeStyle1NlBeforeRegion = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 4,
        preventEmptyBlocks = true,
    )

    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val repo1NL = CLikeCodestyleRepo(codeStyle1NlBeforeRegion)
    val formatter = CodeFormatterKotlinUseCaseImpl(repoNoSpace)

    @Test
    fun testConstantsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("object TEST") {
                addSub(ConstantNode().apply {
                    addKeyword("val")
                    addVarName("A")
                    addKeyword(":")
                    addDatatype("int")
                    addKeyword("=")
                    addRValue("1")
                })
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <TAB><ConstantNode>
        //            <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //        </ConstantNode>
        //        <NL>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(7, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Space)
        Assert.assertTrue(outBlock.subs[1] is Keyword)
        Assert.assertTrue(outBlock.subs[2] is NlSeparator)
        Assert.assertTrue(outBlock.subs[3] is Indent)
        Assert.assertTrue(outBlock.subs[4] is ConstantNode)

        val constantNode = outBlock.subs[4] as ConstantNode
        Assert.assertEquals(10, constantNode.subs.size)
    }

}