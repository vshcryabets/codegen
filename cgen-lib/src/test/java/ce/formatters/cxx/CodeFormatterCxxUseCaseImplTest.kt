package ce.formatters.cxx

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterCxxUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.abstractSyntaxTree.addEnumLeaf
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterCxxUseCaseImplTest {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val formatter = CodeFormatterCxxUseCaseImpl(repoNoSpace)

    @Test
    fun testNamespaceIndents() {
        val input = NamespaceBlock("b").apply {
            addSub(RegionImpl()).apply {
                addOutBlock("enum ENUM") {
                    addEnumLeaf("A")
                    addEnumLeaf("B")
                }
            }
        }
        val output = formatter(input)
        // expected result
        // <NamespaceBlock>
        //     <SPACE> <{> <NL>
        //     <Region>
        //          <Indent> <OutBlock>
        //          </OutBlock>
        //          <NL>
        //     </Region>
        //     <}>
        // </NamespaceBlock>
        Assert.assertEquals(5, output.subs.size)
        val region = output.subs[3] as Region
        Assert.assertEquals(3, region.subs.size)
        Assert.assertTrue(region.subs[0] is Indent)
        Assert.assertTrue(region.subs[1] is OutBlock)
        Assert.assertTrue(region.subs[2] is NlSeparator)
        val outblock = region.subs[1] as OutBlock
        Assert.assertEquals(14, outblock.subs.size)
    }
}