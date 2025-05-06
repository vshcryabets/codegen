package ce.formatters.cxx

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.input.addSub
import generators.obj.out.ImportsBlock
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CxxIncludeBlockTest {
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
    val formatter = CodeFormatterUseCaseImpl(repoNoSpace)


    @Test
    fun testStandartInclude() {
        val input = CppHeaderFile().apply {
            addSub(ImportsBlock()).apply {
                addInclude("<cstdint>")
                addInclude("<string>")
                addInclude("MyHeader.h")
            }
        }
        val output = formatter(input)
        // expected result
        // <CppHeaderFile>
        //     <CompilersDerectve />
        //     <nl>
        //     <ImportsBlock>
        //        <ImportLeaf <cstdint>> <nl>
        //        <ImportLeaf <string>> <nl>
        //        <ImportLeaf "MyHeader.h"> <nl>
        //     </ImportsBlock>
        // </Region>
        Assert.assertEquals(3, output.subs.size)
        val cppHeaderFile = output.subs[0] as CppHeaderFile
        Assert.assertEquals(1, cppHeaderFile.subs.size)
        val importsBlock = cppHeaderFile.subs[0] as CppHeaderFile
        Assert.assertEquals(3, importsBlock.subs.size)
    }

}