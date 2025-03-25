package ce.formatters.java

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterJavaUseCaseImpl
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.input.Node
import generators.obj.input.addDatatype
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.Keyword
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.RegionImpl
import generators.obj.out.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaDataClassFormattingTests {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val formatter = CodeFormatterJavaUseCaseImpl(repoNoSpace)
    val xmlReader = XmlTreeReader()


    @Test
    fun testKotlinDeclarationPattern() {
        Assert.assertEquals(1, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Keyword name="static"/>
                    <Datatype name="int"/>
                    <VariableName name="ModeStateOn"/>
                    <Keyword name="="/>
                    <DataValue name="0"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(0, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Datatype name="int"/>
                    <VariableName name="ModeStateOn"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(2, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Keyword name="public"/>
                    <Keyword name="static"/>
                    <Datatype name="int"/>
                    <VariableName name="ModeStateOn"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(-1, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <Keyword name="static"/>
                    <VariableName name="ModeStateOn"/>
                </ConstantNode>
                """.trimIndent()) as Node))

        Assert.assertEquals(-1, formatter.declarationPattern(xmlReader.loadFromString("""
                <ConstantNode>
                    <VariableName name="ModeStateOn"/>
                    <Separator name=","/>
                </ConstantNode>
                """.trimIndent()) as Node))
    }

    @Test
    fun testRecordClassFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("record TEST") {
                addSub(OutBlockArguments().apply {
                    addSub(ArgumentNode()).apply {
                        addDatatype("int")
                        addVarName("A")
                    }
                    addSub(ArgumentNode()).apply {
                        addDatatype("int")
                        addVarName("B")
                    }
                })
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <(> <nl>
        //        <OutBlockArguments>
        //          <TAB>
        //          <ArgumentNode>
        //            <int><SP><A>
        //          </ArgumentNode><,><NL>
        //          <TAB>
        //          <ArgumentNode>
        //            <int><SP><B>
        //          </ArgumentNode>
        //        </OutBLockArguments><NL>
        //        <)> <SP> <{> <NL> <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(9, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is NlSeparator)
        Assert.assertTrue(outBlock.subs[2] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[3] is NlSeparator)
        Assert.assertTrue(outBlock.subs[4] is Keyword)
        Assert.assertTrue(outBlock.subs[5] is Space)
        Assert.assertTrue(outBlock.subs[6] is Keyword)
        Assert.assertTrue(outBlock.subs[7] is NlSeparator)
        Assert.assertTrue(outBlock.subs[8] is Keyword)

        val outBlockArguments = outBlock.subs[2] as OutBlockArguments
        Assert.assertEquals(6, outBlockArguments.subs.size)
    }

}