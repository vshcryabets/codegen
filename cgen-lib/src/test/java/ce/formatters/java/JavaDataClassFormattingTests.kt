package ce.formatters.java

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterJavaUseCaseImpl
import ce.settings.CodeStyle
import generators.obj.input.*
import generators.obj.out.*
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

    @Test
    fun testRecordClassFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("record TEST") {
                addSub(OutBlockArguments().apply {
                    addSub(ArgumentNode()).apply {
                        addDatatype("int")
                        addVarName("A")
                        addSeparator(",")
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