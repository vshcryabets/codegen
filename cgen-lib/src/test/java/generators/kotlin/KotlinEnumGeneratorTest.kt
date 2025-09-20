package generators.kotlin

import ce.defs.DataType
import ce.defs.RValue
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.abstractSyntaxTree.ConstantsEnum
import generators.obj.abstractSyntaxTree.Namespace
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.EnumNode
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinEnumGeneratorTest {
    private val reader = XmlTreeReader()
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)
    private val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repo = CLikeCodestyleRepo(codeStyle)
    val fileGenerator = KotlinFileGenerator()
    val prepareRightValueUseCase = PrepareRightValueUseCase(dataTypeToString)

    @Test
    fun testSimpleEnumWithDefineValues() {
        val process = KotlinEnumGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
            dataTypeToString = dataTypeToString,
            prepareRightValueUseCase = prepareRightValueUseCase
        )

        val tree = NamespaceImpl("data").apply {
            addSub(ConstantsEnum("CryptoCurrency")).apply {
                defaultType(DataType.int16)
                add("OK", 0)
                add("BUSY")
                add("AUTHERR")
                add("PASSLEN")
                add("PASSWRONG", 8)
            }
        }
        val block = tree.subs.first() as ConstantsEnum

        val projectOutput = OutputTree(Target.Kotlin)
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        process(files, block)

        Assert.assertTrue("Dirty flag should be true", mainFile.isDirty)
        Assert.assertEquals(3, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[2] is RegionImpl)
        val region = mainFile.subs[2] as Region
        Assert.assertEquals(1, region.subs.size)
        Assert.assertTrue(region.subs[0] is OutBlock)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(6, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[1] is EnumNode)
        Assert.assertTrue(outBlock.subs[2] is EnumNode)
        Assert.assertTrue(outBlock.subs[3] is EnumNode)
        Assert.assertTrue(outBlock.subs[4] is EnumNode)
        Assert.assertTrue(outBlock.subs[5] is EnumNode)
        val enumNode5 = outBlock.subs[5] as EnumNode
        Assert.assertEquals("PASSWRONG", enumNode5.name)
        Assert.assertEquals(1 , enumNode5.subs.size)
        Assert.assertTrue(enumNode5.subs[0] is Arguments)
        val arguments5 = enumNode5.subs[0] as Arguments
        Assert.assertEquals(1, arguments5.subs.size)
        Assert.assertTrue(arguments5.subs[0] is RValue)

    }

    @Test
    fun testSimpleEnumClass() {
        val item = KotlinEnumGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
            dataTypeToString = dataTypeToString,
            prepareRightValueUseCase = prepareRightValueUseCase
        )

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

        val projectOutput = OutputTree(Target.Kotlin)
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        item(files, block)

        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //        <region>
        //          <CommentsBlock>...</CommentsBlock>
        //          <OutBlock>
        //              <EnumLeaf><BTC></<EnumLeaf>
        //              <EnumLeaf><ETH></<EnumLeaf>
        //              <EnumLeaf><BCH></<EnumLeaf>
        //          </OutBlock>
        //        </region>
        // </FileData>

        Assert.assertTrue("Dirty flag should be true", mainFile.isDirty)
        Assert.assertEquals(3, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[2] is RegionImpl)
        val region = mainFile.subs[2] as Region
        Assert.assertEquals(2, region.subs.size)
        Assert.assertTrue(region.subs[0] is CommentsBlock)
        Assert.assertTrue(region.subs[1] is OutBlock)
        Assert.assertEquals("Enum test", (region.subs[0] as CommentsBlock).subs[0].name)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(3, outBlock.subs.size)
    }
}