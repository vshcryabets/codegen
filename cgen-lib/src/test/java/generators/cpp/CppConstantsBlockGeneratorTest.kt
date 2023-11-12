package generators.cpp

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppConstantsBlockGeneratorTest {

    @Test
    fun test() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)

        val project = ProjectOutput(Target.Cxx)
        val item = CppConstantsBlockGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )
        val headerFile = CppHeaderFile("a", project)
        val cxxFile = CppFileData("b", project)
        val files = listOf(headerFile, cxxFile)
        val namespace = NamespaceImpl("a", TreeRoot)
        val block = namespace.addSub(ConstantsBlock("c")).apply {
            addBlockComment("182TEST_COMMENT")
            defaultType(DataType.int32)
            add("A", 1)
            add("B")
            add("C")
        }
        Assert.assertFalse("Dirty flag should be false in .h before changes", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false in .cpp before changes", cxxFile.isDirty)
        item(files, block)
        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false", cxxFile.isDirty)
        val outNamespace = headerFile.findOrNull(NamespaceBlock::class.java)!!
        Assert.assertEquals(1, outNamespace.subs.size)
        val constantsBlock = outNamespace.findOrNull(RegionImpl::class.java)!!
        Assert.assertEquals(4, constantsBlock.subs.size)
        Assert.assertEquals(CommentsBlock::class.java, constantsBlock.subs[0]::class.java)
        Assert.assertEquals("182TEST_COMMENT", (constantsBlock.subs[0] as CommentsBlock).subs[0].name)
    }
}