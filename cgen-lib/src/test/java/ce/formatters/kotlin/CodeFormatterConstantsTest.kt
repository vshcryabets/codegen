package ce.formatters.kotlin

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.input.Node
import generators.obj.out.ConstantNode
import generators.obj.out.OutBlock
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterConstantsTest {
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
                <ConstantNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <Datatype name="Int"/>
                    <Keyword name="="/>
                    <RValue name="0"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(0, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Keyword name="var"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <Datatype name="Int"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(2, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Keyword name="volatile"/>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <Datatype name="Int"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(-1, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <Datatype name="Int"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(-1, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <VariableName name="ModeStateOn"/>
                    <Separator name=","/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(1, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name="="/>
                    <RValue name="105"/>
                </ConstantNode>
                """.trimIndent()) as Node))
    }

    @Test
    fun testKotlinConstantsBlock() {
        val input = xmlReader.loadFromString("""
            <Region>
            <OutBlock name="object ModeState">
                <ConstantNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name=":"/>
                    <Datatype name="Int"/>
                    <Keyword name="="/>
                    <RValue name="0"/>
                </ConstantNode>
                <ConstantNode>
                    <Keyword name="const"/>
                    <Keyword name="val"/>
                    <VariableName name="ModeStateOff"/>
                    <Keyword name=":"/>
                    <Datatype name="Int"/>
                    <Keyword name="="/>
                    <RValue name="1"/>
                </ConstantNode>
            </OutBlock>
            </Region>
        """.trimIndent()) as Node
        val outputRegion = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <SPACE> <{> <nl>
        //        <Indent>
        //           <ConstantNode ModeStateOn>
        //              <const><SP><val><SP><ModeStateOff><:><SP><Int><SP><=><SP><1>
        //           </ConstantNode> <nl>
        //        <Indent> <ConstantNode ModeStateOff> <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, outputRegion.subs.size)
        val outBlock = outputRegion.subs[0] as OutBlock
        Assert.assertEquals(10, outBlock.subs.size)
        val constNode1 = outBlock.subs[4] as ConstantNode
        Assert.assertEquals(12, constNode1.subs.size)
    }
}