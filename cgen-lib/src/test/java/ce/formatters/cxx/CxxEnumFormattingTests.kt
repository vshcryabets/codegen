package ce.formatters.cxx

import ce.defs.RValue
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterCxxUseCaseImpl
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.abstractSyntaxTree.addEnumLeaf
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.EnumNode
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.Space
import generators.obj.syntaxParseTree.VariableName
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CxxEnumFormattingTests {
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
    val formatter = CodeFormatterCxxUseCaseImpl(repoNoSpace)

    @Test
    fun testSimpleEnumCxx() {
        val input = RegionImpl().apply {
            addOutBlock("enum ENUM") {
                listOf("A","B","C","D").forEachIndexed { idx,name ->
                    addEnumLeaf("").apply {
                        addVarName(name)
                    }
                }
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <Indent>
        //        <EnumNode>
        //          <VarName A>
        //        </EnumNode>
        //        <,> <nl>
        //        <Indent> <EnumNode B> <,> <nl>
        //        <Indent> <EnumNode C> <,> <nl>
        //        <Indent> <EnumNode D> <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(19, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[4] is EnumNode)
        val firstEnumNode = outBlock.subs[4] as EnumNode
        Assert.assertEquals(1 , firstEnumNode.subs.size)
        Assert.assertTrue(firstEnumNode.subs[0] is VariableName)
    }

    @Test
    fun testEnumRawValueCxx() {
        val input = RegionImpl().apply {
            addOutBlock("enum ENUM") {
                listOf("A","B","C","D").forEachIndexed { idx,name ->
                    addEnumLeaf("").apply {
                        addVarName(name)
                        addKeyword("=")
                        addSub(RValue(idx.toString()))
                    }
                }
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <Indent>
        //        <EnumNode>
        //          <VarName A>
        //          <SPACE>
        //          <=>
        //          <SPACE>
        //          <RValue 0>
        //        </EnumNode> <,> <nl>
        //        <Indent>
        //        <EnumNode B>... <,> <nl>
        //        <Indent>
        //        <EnumNode C>... <,> <nl>
        //        <Indent>
        //        <EnumNode D>... <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertTrue(outBlock.subs[0] is Space)
        Assert.assertTrue(outBlock.subs[1] is Keyword)
        Assert.assertTrue(outBlock.subs[2] is NlSeparator)
        Assert.assertEquals(19, outBlock.subs.size)

        Assert.assertTrue(outBlock.subs[4] is EnumNode)
        val firstEnumNode = outBlock.subs[4] as EnumNode
        Assert.assertEquals(5 , firstEnumNode.subs.size)
        Assert.assertTrue(firstEnumNode.subs[0] is VariableName)
        Assert.assertTrue(firstEnumNode.subs[1] is Space)
        Assert.assertTrue(firstEnumNode.subs[2] is Keyword)
        Assert.assertTrue(firstEnumNode.subs[3] is Space)
        Assert.assertTrue(firstEnumNode.subs[4] is RValue)
    }
}