package ce.formatters

import ce.defs.Target
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.addKeyword
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.input.addSub2
import generators.obj.out.*
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

        val project = Region("").apply {
                addSub2(CommentsBlock()) {
                    addSub(CommentLeaf("Line 1"))
                }
                addSub(ConstantLeaf {
                    addSub(Keyword("const"))
                    addSub(Datatype("int32_t"))
                    addSub(VariableName("OREAD"))
                    addSub(Keyword("="))
                    addSub(RValue("0"))
                    addSub(Separator(";"))
                })
                addSub(ConstantLeaf {
                    addKeyword("const")
                    addSub(Datatype("int32_t"))
                    addSub(VariableName("OWRITE"))
                    addKeyword("=")
                    addSub(RValue("1"))
                    addSeparator(";")
                })
        }

        val result = formatter(project) as Region
        Assert.assertEquals(4, result.subs.size) //  newline + namespace + newline
    }
}