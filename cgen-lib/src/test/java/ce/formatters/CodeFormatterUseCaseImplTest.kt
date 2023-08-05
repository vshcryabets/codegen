package ce.formatters

import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.Namespace
import generators.obj.input.NamespaceImpl
import generators.obj.input.addKeyword
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterUseCaseImplTest {
    @Test
    fun testRegion() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 0,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val formatter = CodeFormatterUseCaseImpl(repo)

        val project = NamespaceBlock("ns1").apply {
            addSub(RegionImpl()).apply {
                addSub(CommentsBlock()).apply {
                    addSub(CommentLeaf("Line 1"))
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

        val result = formatter(project) as NamespaceBlock
        // expected result
        // <NamespaceBlock> <{> <nl>
        // <indent> <// CommentsBlock> <nl>
        // <indent> constant1 <nl>
        // <indent> constant2 <nl>
        // <}> </NamespaceBlock>
        Assert.assertEquals(12, result.subs.size)
    }

    @Test
    fun testCxxPragma() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val formatter = CodeFormatterUseCaseImpl(repo)

        val project = CppHeaderFile("ns1").apply {
            addSub(CommentsBlock("a"))
            addSub(NamespaceBlock("b"))
        }

        val result = formatter(project) as CppHeaderFile
        // expected result
        // <CppHeaderFile>
        //    <nl>
        //    <CommentsBlock> <nl>
        //    <NamespaceBlock> <{> <nl>
        //    <}>
        // </CppHeaderFile>
        Assert.assertEquals(7, result.subs.size) //  newline + namespace + newline
    }
}