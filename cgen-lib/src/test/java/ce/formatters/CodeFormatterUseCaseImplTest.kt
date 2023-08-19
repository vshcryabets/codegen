package ce.formatters

import ce.defs.Target
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterUseCaseImplTest {
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
        //     <SPACE> <{> <nl>
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
        Assert.assertEquals(5, output.subs.size)
        val region = output.subs[3] as Region
        Assert.assertEquals(7, region.subs.size)
        val commentBlock = region.subs[0] as CommentsBlock
        Assert.assertEquals(6, commentBlock.subs.size)
    }

    @Test
    fun testCxxPragma() {
        val formatter = CodeFormatterUseCaseImpl(repo1NL)
        val input = CppHeaderFile("ns1").apply {
            addSub(NamespaceBlock("b"))
        }

        val output = formatter(input) as CppHeaderFile
        // expected result
        // <CppHeaderFile>
        //    <pragama once> <nl>
        //    <nl>
        //    <NamespaceBlock>
        //       <SPACE> <{><nl>
        //       <}>
        //    </NamespaceBlock>
        //    <nl>
        // </CppHeaderFile>
        Assert.assertEquals(5, output.subs.size)
        Assert.assertEquals(4, (output.subs[3] as NamespaceBlock).subs.size)
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

    @Test
    fun testNonDirtyFileData() {
        val input = ProjectOutput(target = Target.Cxx).apply {
            addSub(CppHeaderFile("ns1").apply {
                addSub(NamespaceBlock("b"))
            })
            addSub(CppHeaderFile("ns2").apply {
                addSub(NamespaceBlock("b"))
                isDirty = false
            })
        }
        val output = formatter(input)
        Assert.assertEquals(1, output.subs.size)
    }
}