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

class CppConstantsBlockGeneratorTest {
    val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repo = CLikeCodestyleRepo(codeStyle)


    @Test
    fun test() {
        val project = OutputTree(Target.Cxx)
        val item = CppConstantsBlockGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )
        val headerFile = CppHeaderFile("a").apply { setParent2(project) }
        val cxxFile = CppFileData("b").apply { setParent2(project) }
        val files = listOf(headerFile, cxxFile)
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(ConstantsBlock("c")).apply {
            addBlockComment("182TEST_COMMENT")
            defaultType(DataType.int32)
            add("A", 1)
            add("B", DataType.string(false), "ABC")
            add("C")
        }
        Assert.assertFalse("Dirty flag should be false in .h before changes", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false in .cpp before changes", cxxFile.isDirty)
        item(files, block)

        // expected result
        // <FileData>
        //     <CompilerDerective pragma once>
        //     <ImportsBlock>
        //         <ImportLeaf cstdint />
        //         <ImportLeaf string />
        //     </ImportsBlock>
        //     <NamespaceBloack>
        //        <region>
        //          <OutBlock>
        //              <ConstantNode>
        //                  <public><static><final><int><OREAD><=><0>
        //              </ConstantNode>
        //              <ConstantNode />
        //          </OutBlock>
        //        </region>
        //     </NamespaceBloack>
        // </FileData>


        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false", cxxFile.isDirty)

        Assert.assertEquals(3, headerFile.subs.size)
        Assert.assertTrue(headerFile.subs[0] is CompilerDirective)
        Assert.assertTrue(headerFile.subs[1] is ImportsBlock)
        Assert.assertTrue(headerFile.subs[2] is NamespaceBlock)

        val imports = headerFile.subs[1] as ImportsBlock
        Assert.assertEquals(2, imports.subs.size)

        val outNamespace = headerFile.findOrNull(NamespaceBlock::class.java)!!
        Assert.assertEquals(1, outNamespace.subs.size)
        val constantsBlock = outNamespace.findOrNull(RegionImpl::class.java)!!
        Assert.assertEquals(4, constantsBlock.subs.size)
        Assert.assertEquals(CommentsBlock::class.java, constantsBlock.subs[0]::class.java)
        Assert.assertEquals("182TEST_COMMENT", (constantsBlock.subs[0] as CommentsBlock).subs[0].name)
    }
}