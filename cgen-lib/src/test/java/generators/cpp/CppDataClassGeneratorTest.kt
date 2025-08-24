package generators.cpp

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.TreeRoot
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppDataClassGeneratorTest {

    @Test
    fun testSimpleStructure() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)

        val project = OutputTree(Target.Cpp)
        val item = CppDataClassGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )
        val headerFile = CppHeaderFile("a").apply { setParent2(project) }
        val cxxFile = CppFileData("b").apply { setParent2(project) }
        val files = listOf(headerFile, cxxFile)
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(DataClass("c")).apply {
            addBlockComment("182TEST_COMMENT")
            field("A", DataType.int32,  1)
            field("B", DataType.float64,  0.5f)
            field("C", DataType.stringNullable)
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
        //              <FieldNode><int32><A><=><1></<FieldNode>
        //              <FieldNode><float64><B><=><0.5></<FieldNode>
        //              <FieldNode><string><C></<FieldNode>
        //          </OutBlock>
        //        </region>
        //     </namespace>
        // </CppHeaderFile>

        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false", cxxFile.isDirty)
        val outNamespace = headerFile.findOrNull(NamespaceBlock::class.java)!!
        Assert.assertEquals(1, outNamespace.subs.size)
        Assert.assertTrue(outNamespace.subs[0] is Region)
        val region = outNamespace.subs[0] as Region
        Assert.assertEquals(2, region.subs.size)
        Assert.assertEquals(CommentsBlock::class.java, region.subs[0]::class.java)
        Assert.assertEquals("182TEST_COMMENT", (region.subs[0] as CommentsBlock).subs[0].name)
        Assert.assertEquals(OutBlock::class.java, region.subs[1]::class.java)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(3, outBlock.subs.size)

        val field1 = outBlock.subs[0] as FieldNode
        Assert.assertEquals(4, field1.subs.size)
    }
}