package ce.formatters

import ce.settings.CodeStyle
import generators.obj.input.Namespace
import generators.obj.input.NamespaceImpl
import generators.obj.input.addKeyword
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.ConstantLeaf
import generators.obj.out.Datatype
import generators.obj.out.Keyword
import generators.obj.out.RValue
import generators.obj.out.RegionImpl
import generators.obj.out.Separator
import generators.obj.out.VariableName
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterUseCaseImplTest {
    @Test
    fun testRegion() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val formatter = CodeFormatterUseCaseImpl(repo)

        val project = NamespaceImpl("ns1").apply {
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

        val result = formatter(project) as Namespace
        Assert.assertEquals(2, result.subs.size) //  newline + namespace + newline
    }
}