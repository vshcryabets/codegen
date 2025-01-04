package ce.formatters.cxx

import ce.defs.Target
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CxxConstantsFormatterTest {
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
    val formatter = CodeFormatterUseCaseImpl(repoNoSpace)

    @Test
    fun testRegion() {
        val input = NamespaceBlock("ns1").apply {
            addSub(RegionImpl()).apply {
                addSub(CommentsBlock()).apply {
                    addSub(CommentLeaf("Line 1"))
                    addSub(CommentLeaf("Line 2"))
                }
                addSub(ConstantNode().apply {
                    addSub(Keyword("const"))
                    addSub(Datatype("int32_t"))
                    addSub(VariableName("OREAD"))
                    addSub(Keyword("="))
                    addSub(RValue("0"))
                })
                addSub(ConstantNode().apply {
                    addKeyword("const")
                    addSub(Datatype("int32_t"))
                    addSub(VariableName("OWRITE"))
                    addKeyword("=")
                    addSub(RValue("1"))
                })
            }
        }

        val output = formatter(input) as NamespaceBlock
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
        val constantNode1 = region.subs[2] as ConstantNode
        Assert.assertEquals(9, constantNode1.subs.size)
    }

    @Test
    fun testConstantsLeaf() {
        val input = ConstantNode().apply {
            addSub(Keyword("const"))
            addSub(Datatype("int32_t"))
            addSub(VariableName("OREAD"))
            addSub(Keyword("="))
            addSub(RValue("0"))
        }

        val output = formatter(input) as ConstantNode
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