package ce.formatters.kotlin

import ce.defs.RValue
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.input.addDatatype
import generators.obj.input.addEnumLeaf
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.Arguments
import generators.obj.out.EnumNode
import generators.obj.out.Keyword
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.RegionImpl
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinEnumFormattingTests {
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
                listOf("A","B","C","D").forEachIndexed { idx,name ->
                    addEnumLeaf(name).apply {
                        addSub(Arguments())
                            .addSub(RValue(idx.toString()))
                    }
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
        //              <val><SPACE><rawValue><:><SPACE><int>
        //          </ArgumentNode>
        //        </OutBLockArguments>
        //        <)>
        //        <SPACE> <{> <nl>
        //        <Indent>
        //        <EnumNode A>
        //            <(><RValue 0><)>
        //        </EnumNode> <,> <nl>
        //        <Indent> <EnumNode B>... <,> <nl>
        //        <Indent> <EnumNode C>... <,> <nl>
        //        <Indent> <EnumNode D>... <nl>
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

        Assert.assertTrue(outBlock.subs[7] is EnumNode)
        val firstEnumNode = outBlock.subs[7] as EnumNode
        Assert.assertEquals(3 , firstEnumNode.subs.size)
    }
}