package ce.writers

import ce.formatters.CLikeCodestyleRepo
import ce.io.CodeWriter
import ce.repository.ReportsRepoImpl
import ce.settings.CodeStyle
import generators.cpp.CppWritter
import generators.obj.abstractSyntaxTree.addSubs
import generators.obj.syntaxParseTree.ImportLeaf
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.NlSeparator
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppWritterTest {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val reportsRepo = ReportsRepoImpl()
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val writter = CppWritter(
        repoNoSpace, "",
        reportsRepo = reportsRepo
    )

    @Test
    fun testIncludeLeafWithLtGt() {
        val input = ImportsBlock().apply {
            addSubs(
                ImportLeaf("<iostream>"),
                NlSeparator(),
                ImportLeaf("mylib.h"),
                NlSeparator(),
            )
        }
        val buffer = StringBuffer()
        writter.writeNode(input, object : CodeWriter {
            override fun write(str: String): CodeWriter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWriter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWriter = this
            override fun setIndent(str: String): CodeWriter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("#include <iostream>\n#include \"mylib.h\"\n", buffer.toString())
    }
}