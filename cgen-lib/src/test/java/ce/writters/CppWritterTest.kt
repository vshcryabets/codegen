package ce.writters

import ce.formatters.CLikeCodestyleRepo
import ce.io.CodeWritter
import ce.repository.ReportsRepoImpl
import ce.settings.CodeStyle
import generators.cpp.CppWritter
import generators.obj.input.addSubs
import generators.obj.out.ImportLeaf
import generators.obj.out.ImportsBlock
import generators.obj.out.NlSeparator
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
        writter.writeNode(input, object : CodeWritter {
            override fun write(str: String): CodeWritter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWritter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWritter = this
            override fun setIndent(str: String): CodeWritter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("#include <iostream>\n#include \"mylib.h\"\n", buffer.toString())
    }
}