package ce.formatters.kotlin

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.input.Node
import generators.obj.input.addDatatype
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addRValue
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.FieldNode
import generators.obj.out.Indent
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.RegionImpl
import generators.obj.out.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinConstantsFormatterTest {
    val xmlReader = XmlTreeReader()

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
    fun testKotlinDeclarationPattern() {
        Assert.assertEquals(1, formatter.declarationPattern(xmlReader.loadFromString("""
                <FieldNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <AstTypeLeaf name="Int"/>
                    <Keyword name="="/>
                    <DataValue name="0"/>
                </FieldNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(0, formatter.declarationPattern(xmlReader.loadFromString("""
                <FieldNode>
                    <Keyword name="var"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <AstTypeLeaf name="Int"/>
                </FieldNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(2, formatter.declarationPattern(xmlReader.loadFromString("""
                <FieldNode>
                    <Keyword name="volatile"/>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <AstTypeLeaf name="Int"/>
                </FieldNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(-1, formatter.declarationPattern(xmlReader.loadFromString("""
                <FieldNode>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <AstTypeLeaf name="Int"/>
                </FieldNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(-1, formatter.declarationPattern(xmlReader.loadFromString("""
                <FieldNode>
                    <VariableName name="ModeStateOn"/>
                    <Separator name=","/>
                </FieldNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(1, formatter.declarationPattern(xmlReader.loadFromString("""
                <FieldNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name="="/>
                    <DataValue name="105"/>
                </FieldNode>
                """.trimIndent()) as Node))
    }

    @Test
    fun testKotlinConstantsBlock() {
        val input = xmlReader.loadFromString("""
            <Region>
            <OutBlock name="object ModeState">
                <FieldNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <AstTypeLeaf name="Int"/>
                    <Keyword name="="/>
                    <DataValue name="0"/>
                </FieldNode>
                <FieldNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOff"/>
                    <Keyword name=":"/>
                    <AstTypeLeaf name="Int"/>
                    <Keyword name="="/>
                    <DataValue name="1"/>
                </FieldNode>
            </OutBlock>
            </Region>
        """.trimIndent()) as Node
        val outputRegion = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <TAB>
        //        <ConstantNode ModeStateOn>
        //            <const><SP><val><SP><ModeStateOff><:><SP><Int><SP><=><SP><1>
        //        </ConstantNode> <nl>
        //        <TAB> <ConstantNode ModeStateOff> <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, outputRegion.subs.size)
        val outBlock = outputRegion.subs[0] as OutBlock
        Assert.assertEquals(10, outBlock.subs.size)
        val constNode1 = outBlock.subs[4] as FieldNode
        Assert.assertEquals(12, constNode1.subs.size)
    }


    @Test
    fun testConstantsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("object TEST") {
                addSub(FieldNode().apply {
                    addKeyword("val")
                    addVarName("A")
                    addKeyword(":")
                    addDatatype("int")
                    addKeyword("=")
                    addRValue("1")
                })
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <TAB><ConstantNode>
        //            <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //        </ConstantNode>
        //        <NL>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(7, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Space)
        Assert.assertTrue(outBlock.subs[1] is Keyword)
        Assert.assertTrue(outBlock.subs[2] is NlSeparator)
        Assert.assertTrue(outBlock.subs[3] is Indent)
        Assert.assertTrue(outBlock.subs[4] is FieldNode)

        val constantNode = outBlock.subs[4] as FieldNode
        Assert.assertEquals(10, constantNode.subs.size)
    }
}