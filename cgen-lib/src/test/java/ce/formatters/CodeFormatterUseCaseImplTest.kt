package ce.formatters

import ce.defs.Target
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterUseCaseImplTest {
    @Test
    fun testIndents() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val formatter = CodeFormatterUseCaseImpl(repo)

        val project = ProjectOutput(Target.Cxx)

        project.addSub2(CppHeaderFile("test.h", project)) {
            addSub2(NamespaceBlock("NS")) {
                addSub2(Region("")) {
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
            }
        }
        val result = formatter(project)
        Assert.assertEquals(1, result.subs.size) // 1 file
        val file = result.subs[0]
        Assert.assertEquals(4, result.subs.size) // headers + newline + namespace + newline
    }
}