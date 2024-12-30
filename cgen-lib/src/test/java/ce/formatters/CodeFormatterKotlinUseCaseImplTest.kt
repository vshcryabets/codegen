package ce.formatters

import ce.settings.CodeStyle
import generators.obj.input.addDatatype
import generators.obj.input.addEnumLeaf
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addRValue
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.Indent
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.RegionImpl
import generators.obj.out.Separator
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterKotlinUseCaseImplTest {
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
    val formatter = CodeFormatterKotlinUseCaseImpl(repoNoSpace)

    @Test
    fun testSimpleEnumKotlin() {
        val input = RegionImpl().apply {
            addOutBlock("enum class ENUM") {
                addEnumLeaf("A")
                addEnumLeaf("B")
                addEnumLeaf("C")
                addEnumLeaf("D")
            }
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
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(19, outBlock.subs.size)
    }

    @Test
    fun testEnumRawValueKotlin() {
        val input = RegionImpl().apply {
            addOutBlock("enum class ENUM") {
                addSub(OutBlockArguments()).apply {
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("rawValue")
                        addKeyword(":")
                        addDatatype("int")
                    })
                }
                addEnumLeaf("A(0)")
                addEnumLeaf("B(1)")
                addEnumLeaf("C(2)")
                addEnumLeaf("D(3)")
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <(>
        //        <OutBlockArguments>
        //          <ArgumentNode>
        //              <val><SPACE><rawValue><:><SPACE><int>
        //          </ArgumentNode>
        //        </OutBLockArguments>
        //        <)>
        //        <SPACE> <{> <nl>
        //        <Indent> <EnumLeaf A> <,> <nl>
        //        <Indent> <EnumLeaf B> <,> <nl>
        //        <Indent> <EnumLeaf C> <,> <nl>
        //        <Indent> <EnumLeaf D> <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[2] is Keyword)
        Assert.assertEquals(22, outBlock.subs.size)

        val outBlockArguments = outBlock.subs[1] as OutBlockArguments
        Assert.assertEquals(1, outBlockArguments.subs.size)

        Assert.assertTrue(outBlockArguments.subs[0] is ArgumentNode)
        val argumentNode = outBlockArguments.subs[0] as ArgumentNode
        Assert.assertTrue(argumentNode.subs[0] is Keyword)
        Assert.assertEquals(6, argumentNode.subs.size)
    }

    @Test
    fun testDataClassOneArgumentsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("data class TEST") {
                addSub(OutBlockArguments()).apply {
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("A")
                        addKeyword(":")
                        addDatatype("int")
                        addKeyword("=")
                        addRValue("1")
                    })
                }
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <(>
        //        <OutBlockArguments>
        //          <ArgumentNode>
        //              <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //          </ArgumentNode>
        //        </OutBLockArguments>
        //        <)>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[2] is Keyword)
        Assert.assertEquals(3, outBlock.subs.size)

        val outBlockArguments = outBlock.subs[1] as OutBlockArguments
        Assert.assertEquals(1, outBlockArguments.subs.size)

        Assert.assertTrue(outBlockArguments.subs[0] is ArgumentNode)
        val argumentNode = outBlockArguments.subs[0] as ArgumentNode
        Assert.assertTrue(argumentNode.subs[0] is Keyword)
        Assert.assertEquals(10, argumentNode.subs.size)
    }

    @Test
    fun testDataClassMultimpleArgumentsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("data class TEST") {
                addSub(OutBlockArguments()).apply {
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("A")
                        addKeyword(":")
                        addDatatype("int")
                        addKeyword("=")
                        addRValue("1")
                    })
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("B")
                        addKeyword(":")
                        addDatatype("float")
                        addKeyword("=")
                        addRValue("0.5f")
                    })
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("V")
                        addKeyword(":")
                        addDatatype("String?")
                    })
                }
            }
        }
        val output = formatter(input)

        // expected result
        // <Region>
        //     <OutBlock>
        //        <(> <NL>
        //        <OutBlockArguments>
        //          <TAB>
        //          <ArgumentNode>
        //              <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //          </ArgumentNode><,><NL>
        //          <TAB>
        //          <ArgumentNode>
        //              <val><SPACE><B><:><SPACE><float><SPACE><=><SPACE><0.5f>
        //          </ArgumentNode><,><NL>
        //          <TAB>
        //          <ArgumentNode>
        //              <val><SPACE><C><:><SPACE><String?><NL>
        //          </ArgumentNode>
        //        </OutBLockArguments><NL>
        //        <)>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(5, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is NlSeparator)
        Assert.assertTrue(outBlock.subs[2] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[3] is NlSeparator)
        Assert.assertTrue(outBlock.subs[4] is Keyword)

        val outBlockArguments = outBlock.subs[2] as OutBlockArguments
        Assert.assertEquals(10, outBlockArguments.subs.size)

        Assert.assertTrue(outBlockArguments.subs[0] is Indent)
        Assert.assertTrue(outBlockArguments.subs[1] is ArgumentNode)
        Assert.assertTrue(outBlockArguments.subs[2] is Separator)
        Assert.assertTrue(outBlockArguments.subs[3] is NlSeparator)
        Assert.assertTrue(outBlockArguments.subs[4] is Indent)
        Assert.assertTrue(outBlockArguments.subs[5] is ArgumentNode)
        Assert.assertTrue(outBlockArguments.subs[6] is Separator)
        Assert.assertTrue(outBlockArguments.subs[7] is NlSeparator)
        Assert.assertTrue(outBlockArguments.subs[8] is Indent)
        Assert.assertTrue(outBlockArguments.subs[9] is ArgumentNode)

        val argumentNode = outBlockArguments.subs[1] as ArgumentNode
        Assert.assertTrue(argumentNode.subs[0] is Keyword)
        Assert.assertEquals(10, argumentNode.subs.size)
    }
}