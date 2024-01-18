package generators.cpp

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppEnumGeneratorTest {

    @Test
    fun testWithIntValues() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)

        val project = OutputTree(Target.Cxx)
        val item = CppEnumGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )
        val headerFile = CppHeaderFile("a").apply { setParent2(project) }
        val cxxFile = CppFileData("b").apply { setParent2(project) }
        val files = listOf(headerFile, cxxFile)
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(ConstantsEnum("c")).apply {
            addBlockComment("182TEST_COMMENT")
            defaultType(DataType.int32)
            add("A", 1)
            add("B", 2)
            add("C", 33)
        }
        Assert.assertFalse("Dirty flag should be false in .h before changes", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false in .cpp before changes", cxxFile.isDirty)
        item(files, block)

        // expected result
        // <CppHeaderFile>
        //     <pragma once>
        //     <namespace>
        //        <region>
        //          <CommentsBlock>...</CommentsBlock>
        //          <OutBlock>
        //              <EnumLeaf><A><=><1></<EnumLeaf>
        //              <EnumLeaf><B><=><2></<EnumLeaf>
        //              <EnumLeaf><C><=><33></<EnumLeaf>
        //          </OutBlock>
        //        </region>
        //     </namespace>
        // </CppHeaderFile>


        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false", cxxFile.isDirty)
        val outNamespace = headerFile.findOrNull(NamespaceBlock::class.java)!!
        Assert.assertEquals(1, outNamespace.subs.size)
        val region = outNamespace.findOrNull(RegionImpl::class.java)!!
        Assert.assertEquals(2, region.subs.size)
        Assert.assertEquals(CommentsBlock::class.java, region.subs[0]::class.java)
        Assert.assertEquals("182TEST_COMMENT", (region.subs[0] as CommentsBlock).subs[0].name)
        Assert.assertEquals(OutBlock::class.java, region.subs[1]::class.java)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(3, outBlock.subs.size)

    }
}