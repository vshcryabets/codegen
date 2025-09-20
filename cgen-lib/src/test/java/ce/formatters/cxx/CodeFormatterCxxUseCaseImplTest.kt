package ce.formatters.cxx

import ce.defs.DataType
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterCxxUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addEnumLeaf
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addRValue
import generators.obj.abstractSyntaxTree.addSeparator
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.Indent
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.Separator
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
    fun testSimpleEnum() {
        val input = RegionImpl().apply {
            addOutBlock("enum ENUM") {
                addEnumLeaf("A")
                addEnumLeaf("B")
                addEnumLeaf("C")
                addEnumLeaf("D")
            }
            addSeparator(";")
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <Indent> <EnumLeaf A> <,> <nl>
        //        <Indent> <EnumLeaf B> <,> <nl>
        //        <Indent> <EnumLeaf C> <,> <nl>
        //        <Indent> <EnumLeaf D> <nl>
        //        <}>
        //     </OutBlock>
        //     <;>
        //     <NL>
        // </Region>
        Assert.assertEquals(3, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(19, outBlock.subs.size)
        Assert.assertEquals(Separator::class.java, output.subs[1]::class.java)
    }

    @Test
    fun testEnumWithValue() {
        val input = RegionImpl().apply {
            addOutBlock("enum class ENUM") {
                addSub(OutBlockArguments()).apply {
                    addSub(DataField("name").apply {
                        setType(DataType.int16)
                    })
                }
                addEnumLeaf("A").apply {
                    addVarName("A")
                    addKeyword("=")
                    addRValue("0")
                }
                addEnumLeaf("B").apply {
                    addVarName("B")
                    addKeyword("=")
                    addRValue("1")
                }
                addEnumLeaf("C").apply {
                    addVarName("C")
                    addKeyword("=")
                    addRValue("2")
                }
                addEnumLeaf("D").apply {
                    addVarName("D")
                    addKeyword("=")
                    addRValue("3")
                }
            }
            addSeparator(";")
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <(> <OutBLockArguments> <)>
        //        <SPACE> <{> <nl>
        //        <Indent> <EnumLeaf A> <,> <nl>
        //        <Indent> <EnumLeaf B> <,> <nl>
        //        <Indent> <EnumLeaf C> <,> <nl>
        //        <Indent> <EnumLeaf D> <nl>
        //        <}>
        //     </OutBlock>
        //     <;>
        //     <NL>
        // </Region>
        Assert.assertEquals(3, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[2] is Keyword)
        Assert.assertEquals(22, outBlock.subs.size)
        Assert.assertEquals(Separator::class.java, output.subs[1]::class.java)
    }

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