package generators.cpp

import ce.defs.DataType
import ce.defs.RValue
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.TreeRoot
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.syntaxParseTree.AstTypeLeaf
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.VariableName
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppConstantsBlockGeneratorTest {
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val prepareRightValueUseCase = PrepareRightValueUseCase(getTypeNameUseCase)
    val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repo = CLikeCodestyleRepo(codeStyle)
    val item = CppConstantsBlockGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
        prepareRightValueUseCase = prepareRightValueUseCase
    )
    val fileGenerator = CppFileGenerator()

    @Test
    fun testConstantsBlock() {
        val projectOutput = OutputTree(Target.Cpp)

        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(ConstantsBlock("c")).apply {
            addBlockComment("182TEST_COMMENT")
            defaultType(DataType.int32)
            add("A", 1)
            add("B")
            add("C")
        }

        val files = fileGenerator.createFile(projectOutput,
            workingDirectory = "./",
            packageDirectory = "",
            "a", block)
        val headerFile = files.first { it is CppHeaderFile } as CppHeaderFile
        val cxxFile = files.first { it is CppFileData } as CppFileData

        Assert.assertFalse("Dirty flag should be false in .h before changes", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false in .cpp before changes", cxxFile.isDirty)
        item(files, block)
        // expected result
        // <CppHeaderFile name="a">
        //     <FileMetaInformation />
        //     <CompileDirective pragma once/>
        //     <ImportsBlock />
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

        Assert.assertEquals(4, headerFile.subs.size)
        Assert.assertEquals(CompilerDirective::class, headerFile.subs[1]::class)
        Assert.assertEquals(ImportsBlock::class, headerFile.subs[2]::class)
        Assert.assertEquals(NamespaceBlock::class, headerFile.subs[3]::class)

        val outNamespace = headerFile.subs[3] as NamespaceBlock
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