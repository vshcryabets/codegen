package ce.formatters.cxx

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.input.addKeyword
import generators.obj.input.addRValue
import generators.obj.input.addSub
import generators.obj.out.AstTypeLeaf
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.FieldNode
import generators.obj.out.Keyword
import generators.obj.out.NamespaceBlock
import generators.obj.out.Region
import generators.obj.out.RegionImpl
import generators.obj.out.VariableName
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CxxConstantsFormatterTest {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val formatter = CodeFormatterUseCaseImpl(repoNoSpace)

    @Test
    fun testRegion() {
        val input = NamespaceBlock("ns1").apply {
            addSub(RegionImpl()).apply {
                addSub(CommentsBlock()).apply {
                    addSub(CommentLeaf("Line 1"))
                    addSub(CommentLeaf("Line 2"))
                }
                addSub(FieldNode().apply {
                    addSub(Keyword("const"))
                    addSub(AstTypeLeaf("int32_t"))
                    addSub(VariableName("OREAD"))
                    addSub(Keyword("="))
                    addRValue("0")
                })
                addSub(FieldNode().apply {
                    addKeyword("const")
                    addSub(AstTypeLeaf("int32_t"))
                    addSub(VariableName("OWRITE"))
                    addKeyword("=")
                    addRValue("1")
                })
            }
        }

        val output = formatter(input)
        // expected result
        // <NamespaceBlock>
        //     <SPACE> <{> <nl>
        //     // no region pre new lines - because of "newLinesBeforeClass = 0"
        //     <Region>
        //         <CommentBlock>
        //             <indent> Line1 <nl>
        //             <indent> Line2 <nl>
        //         </CommentBlock>
        //        <TAB><ConstantNode>
        //            <const><SP><int32_t><SP><OREAD><SP><=><SP><0>
        //        </ConstantNode>
        //        <;><NL>
        //        <TAB><ConstantNode>
        //            <const><SP><int32_t><SP><OWRITE><SP><=><SP><1>
        //        </ConstantNode>
        //        <;><NL>
        //     </Region>
        //     <}>
        // </NamespaceBlock>
        Assert.assertEquals(5, output.subs.size)
        val region = output.subs[3] as Region
        Assert.assertEquals(9, region.subs.size)
        val commentBlock = region.subs[0] as CommentsBlock
        Assert.assertEquals(6, commentBlock.subs.size)
        val constantNode1 = region.subs[2] as FieldNode
        Assert.assertEquals(9, constantNode1.subs.size)
    }

    @Test
    fun testConstantsLeaf() {
        val input = FieldNode().apply {
            addSub(Keyword("const"))
            addSub(AstTypeLeaf("int32_t"))
            addSub(VariableName("OREAD"))
            addSub(Keyword("="))
            addRValue("0")
        }

        val output = formatter(input) as FieldNode
        // expected result
        // <ConstantLeaf>
        //     <const> <SPACE>
        //     <int32_t> <SPACE>
        //     <OREAD> <SPACE>
        //     <=> <SPACE>
        //     <0>
        // </ConstantLeaf>
        Assert.assertEquals(9, output.subs.size)
    }

}