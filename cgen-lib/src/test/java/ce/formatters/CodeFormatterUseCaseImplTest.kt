package ce.formatters

import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.addKeyword
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterUseCaseImplTest {
    val codeStyle = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repo = CLikeCodestyleRepo(codeStyle)
    val formatter = CodeFormatterUseCaseImpl(repo)

    @Test
    fun testRegion() {
        val input = NamespaceBlock("ns1").apply {
            addSub(RegionImpl()).apply {
                addSub(CommentsBlock()).apply {
                    addSub(CommentLeaf("Line 1"))
                    addSub(CommentLeaf("Line 2"))
                }
                addSub(ConstantLeaf().apply {
                    addSub(Keyword("const"))
                    addSub(Datatype("int32_t"))
                    addSub(VariableName("OREAD"))
                    addSub(Keyword("="))
                    addSub(RValue("0"))
                    addSub(Separator(";"))
                })
                addSub(ConstantLeaf().apply {
                    addKeyword("const")
                    addSub(Datatype("int32_t"))
                    addSub(VariableName("OWRITE"))
                    addKeyword("=")
                    addSub(RValue("1"))
                    addSeparator(";")
                })
            }
        }

        val output = formatter(input) as NamespaceBlock
        // expected result
        // <NamespaceBlock>
        //     <{> <nl>
        //     // no region pre new lines - because of "newLinesBeforeClass = 0"
        //     <Region>
        //         <CommentBlock>
        //             <indent> Line1 <nl>
        //             <indent> Line2 <nl>
        //         </CommentBlock>
        //         <indent> constant1 <nl>
        //         <indent> constant2 <nl>
        //     </Region>
        //     <}>
        // </NamespaceBlock>
        Assert.assertEquals(4, output.subs.size)
        val region = output.subs[2] as Region
        Assert.assertEquals(7, region.subs.size)
        val commentBlock = region.subs[0] as CommentsBlock
        Assert.assertEquals(6, commentBlock.subs.size)
    }

    @Test
    fun testCxxPragma() {
        val input = CppHeaderFile("ns1").apply {
            addSub(NamespaceBlock("b"))
        }

        val output = formatter(input) as CppHeaderFile
        // expected result
        // <CppHeaderFile>
        //    <pragama once> <nl>
        //    <NamespaceBlock>
        //       <{><nl>
        //       <}>
        //    </NamespaceBlock>
        //    <nl>
        // </CppHeaderFile>
        Assert.assertEquals(4, output.subs.size)
        Assert.assertEquals(3, (output.subs[2] as NamespaceBlock).subs.size)
    }

    @Test
    fun testConstantsLeaf() {
        val input = ConstantLeaf().apply {
            addSub(Keyword("const"))
            addSub(Datatype("int32_t"))
            addSub(VariableName("OREAD"))
            addSub(Keyword("="))
            addSub(RValue("0"))
            addSub(Separator(";"))
        }

        val output = formatter(input) as ConstantLeaf
        // expected result
        // <ConstantLeaf>
        //     <const> <SPACE>
        //     <int32_t> <SPACE>
        //     <OREAD> <SPACE>
        //     <=> <SPACE>
        //     <0>
        //     <;>
        // </ConstantLeaf>
        Assert.assertEquals(10, output.subs.size)
    }
}