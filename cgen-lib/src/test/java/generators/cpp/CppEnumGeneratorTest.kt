package generators.cpp

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.abstractSyntaxTree.*
import generators.obj.syntaxParseTree.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppEnumGeneratorTest {
    private val reader = XmlTreeReader()
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)
    private val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repo = CLikeCodestyleRepo(codeStyle)
    val fileGenerator = CppFileGenerator()
    val prepareRightValueUseCase = PrepareRightValueUseCase(dataTypeToString)
    val item = CppEnumGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
        prepareRightValueUseCase = prepareRightValueUseCase
    )

    @Test
    fun testWithIntValues() {
        val project = OutputTree(Target.Cpp)
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

    @Test
    fun testSimpleEnumClass() {
        val tree = reader.loadFromString("""
            <Namespace name="data">
                <ConstantsEnum name="CryptoCurrency">
                    <CommentsBlock>
                        <CommentLeaf name="Enum test"/>
                    </CommentsBlock>
                    <DataField name="BTC"/>
                    <DataField name="ETH"/>
                    <DataField name="BCH"/>
                </ConstantsEnum>
            </Namespace>
        """.trimIndent()) as Namespace
        val block = tree.subs.first() as ConstantsEnum

        val projectOutput = OutputTree(Target.Cpp)
        val files = fileGenerator.createFile(projectOutput,
            workingDirectory = "./",
            packageDirectory = "",
            "a", block)
        val headerFile = files.first { it is CppHeaderFile }
        item(files, block)

        // expected result
        // <CppHeaderFile>
        //     <FileMetaInformation />
        //     <pragma once>
        //     <ImportsBlock />
        //     <namespace>
        //       <region>
        //         <CommentsBlock>...</CommentsBlock>
        //         <OutBlock>
        //           <EnumLeaf><BTC></<EnumLeaf>
        //           <EnumLeaf><ETH></<EnumLeaf>
        //           <EnumLeaf><BCH></<EnumLeaf>
        //         </OutBlock>
        //       </region>
        //     </namespace>
        // </CppHeaderFile>

        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertEquals(4, headerFile.subs.size)
        Assert.assertTrue(headerFile.subs[3] is NamespaceBlock)
        val nsBlock = headerFile.subs[3] as NamespaceBlock
        Assert.assertEquals(1, nsBlock.subs.size)
        Assert.assertTrue(nsBlock.subs[0] is RegionImpl)
        val region = nsBlock.subs[0] as Region
        Assert.assertEquals(2, region.subs.size)
        Assert.assertTrue(region.subs[0] is CommentsBlock)
        Assert.assertTrue(region.subs[1] is OutBlock)
        Assert.assertEquals("Enum test", (region.subs[0] as CommentsBlock).subs[0].name)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(3, outBlock.subs.size)
    }
}