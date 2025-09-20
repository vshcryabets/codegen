package generators.java

import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.abstractSyntaxTree.ConstantsEnum
import generators.obj.abstractSyntaxTree.Namespace
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaEnumGeneratorTest {
    private val reader = XmlTreeReader()

    @Test
    fun testSimpleEnumClass() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val fileGenerator = JavaFileGenerator()
        val item = JavaEnumGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
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
        val files = fileGenerator.createFile(projectOutput,
            workingDirectory = "./",
            packageDirectory = "",
            "a", block)
        val mainFile = files.first()
        item(files, block)

        // expected result
        // <FileData>
        //     <FileMetaInformation />
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
        Assert.assertEquals(4, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[3] is RegionImpl)
        val region = mainFile.subs[3] as Region
        Assert.assertEquals(2, region.subs.size)
        Assert.assertTrue(region.subs[0] is CommentsBlock)
        Assert.assertTrue(region.subs[1] is OutBlock)
        Assert.assertEquals("Enum test", (region.subs[0] as CommentsBlock).subs[0].name)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(3, outBlock.subs.size)
    }
}