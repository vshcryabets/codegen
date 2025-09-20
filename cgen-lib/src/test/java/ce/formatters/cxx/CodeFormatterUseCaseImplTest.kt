package ce.formatters.cxx

import ce.defs.Target
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.cpp.CppHeaderFile
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.OutputTree
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CodeFormatterUseCaseImplTest {
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
    fun testCxxPragma() {
        val formatter = CodeFormatterUseCaseImpl(repo1NL)
        val input = CppHeaderFile("ns1").apply {
            addSub(NamespaceBlock("b"))
        }

        val output = formatter(input)
        // expected result
        // <CppHeaderFile>
        //    <pragma once> <nl>
        //    <nl>
        //    <NamespaceBlock>
        //       <SPACE> <{><nl>
        //       <}>
        //    </NamespaceBlock>
        //    <nl>
        // </CppHeaderFile>
        Assert.assertEquals(5, output.subs.size)
        Assert.assertEquals(4, (output.subs[3] as NamespaceBlock).subs.size)
    }

    @Test
    fun testNonDirtyFileData() {
        val input = OutputTree(target = Target.Cpp).apply {
            addSub(CppHeaderFile("ns1").apply {
                addSub(NamespaceBlock("b"))
            })
            addSub(CppHeaderFile("ns2").apply {
                addSub(NamespaceBlock("b"))
                isDirty = false
            })
        }
        val output = formatter(input)
        Assert.assertEquals(1, output.subs.size)
    }
}