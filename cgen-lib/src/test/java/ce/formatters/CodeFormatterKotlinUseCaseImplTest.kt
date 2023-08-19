package ce.formatters

import ce.defs.DataType
import ce.defs.Target
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.*
import generators.obj.out.*
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
        //        <Indent> <EnumLeaf D> <,> <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(20, outBlock.subs.size)
    }

    @Test
    fun testEnumRawValueKotlin() {
        val input = RegionImpl().apply {
            addOutBlock("enum class ENUM") {
                addSub(OutBlockArguments()).apply {
                    addSub(DataField("name", DataType.int16))
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
        //        <(> <OutBLockArguments> <)>
        //        <SPACE> <{> <nl>
        //        <Indent> <EnumLeaf A> <,> <nl>
        //        <Indent> <EnumLeaf B> <,> <nl>
        //        <Indent> <EnumLeaf C> <,> <nl>
        //        <Indent> <EnumLeaf D> <,> <nl>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[2] is Keyword)
        Assert.assertEquals(23, outBlock.subs.size)
    }
}