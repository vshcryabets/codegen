package generators.cpp

import ce.defs.DataType
import ce.defs.RValue
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import generators.kotlin.GetArrayDataTypeUseCase
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.PrepareRightValueUseCase
import generators.obj.input.ConstantsBlock
import generators.obj.input.NamespaceImpl
import generators.obj.input.TreeRoot
import generators.obj.input.addSub
import generators.obj.input.findOrNull
import generators.obj.out.AstTypeLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.FieldNode
import generators.obj.out.Keyword
import generators.obj.out.NamespaceBlock
import generators.obj.out.OutputTree
import generators.obj.out.RegionImpl
import generators.obj.out.VariableName
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppConstantsBlockGeneratorTest {
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val prepareRightValueUseCase = PrepareRightValueUseCase(getTypeNameUseCase)

    @Test
    fun testConstantsBlock() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)

        val project = OutputTree(Target.Cpp)
        val item = CppConstantsBlockGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
            prepareRightValueUseCase = prepareRightValueUseCase
        )
        val headerFile = CppHeaderFile("a").apply { setParent2(project) }
        val cxxFile = CppFileData("b").apply { setParent2(project) }
        val files = listOf(headerFile, cxxFile)
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
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
        // expected result
        // <CppHeaderFile name="a">
        //     <CompileDirective pragma once/>
        //     <NamespaceBlock name="a">
        //         <region>
        //             <CommentsBlock>
        //                 <CommentLeaf name="182TEST_COMMENT"/>
        //             </CommentsBlock>
        //             <FieldNode>
        //                <Keyword const>
        //                <AstTypeLeaf int32_t>
        //                <VariableName A>
        //                <Keyword =>
        //                <RValue 0>
        //             </FieldNode>
        //             ....
        //         </region>
        //     </NamespaceBlock>
        // </CppHeaderFile>
        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false", cxxFile.isDirty)
        val outNamespace = headerFile.findOrNull(NamespaceBlock::class.java)!!
        Assert.assertEquals(1, outNamespace.subs.size)
        val constantsBlock = outNamespace.findOrNull(RegionImpl::class.java)!!
        Assert.assertEquals(4, constantsBlock.subs.size)
        Assert.assertEquals(CommentsBlock::class.java, constantsBlock.subs[0]::class.java)
        Assert.assertEquals("182TEST_COMMENT", (constantsBlock.subs[0] as CommentsBlock).subs[0].name)
        Assert.assertEquals(FieldNode::class, constantsBlock.subs[1]::class)
        val constant1 = constantsBlock.subs[1] as FieldNode
        Assert.assertEquals(5, constant1.subs.size)
        Assert.assertEquals(Keyword::class, constant1.subs[0]::class)
        Assert.assertEquals(AstTypeLeaf::class, constant1.subs[1]::class)
        Assert.assertEquals(VariableName::class, constant1.subs[2]::class)
        Assert.assertEquals(Keyword::class, constant1.subs[3]::class)
        Assert.assertEquals(RValue::class, constant1.subs[4]::class)
    }
}