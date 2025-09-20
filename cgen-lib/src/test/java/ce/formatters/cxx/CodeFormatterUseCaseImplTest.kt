package ce.formatters.cxx

import ce.defs.Target
import ce.defs.target
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.cpp.CppFileGenerator
import generators.cpp.CppHeaderFile
import generators.obj.abstractSyntaxTree.ConstantsEnum
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
        val cppFileGenerator = CppFileGenerator()
        val formatter = CodeFormatterUseCaseImpl(repo1NL)
        val input = cppFileGenerator.createFile(
            project = OutputTree(
                target = Target.Cpp
            ),
            workingDirectory = "/tmp",
            packageDirectory = "",
            outputFile = "ns1",
            block = ConstantsEnum("b")
        )
        input[1].addSub(NamespaceBlock("b"))

        val output = formatter(input[1])
        // expected result
        // <CppHeaderFile>
        //    <FileMetaInformation>/tmp</WorkingDirectory>
        //    <pragma once> <nl>
        //    <nl>
        //    <NamespaceBlock>
        //       <SPACE> <{><nl>
        //       <}>
        //    </NamespaceBlock>
        //    <nl>
        // </CppHeaderFile>
        Assert.assertEquals(6, output.subs.size)
        Assert.assertEquals(4, (output.subs[4] as NamespaceBlock).subs.size)
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