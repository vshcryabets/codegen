package generators.java

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
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaDataClassGeneratorTest {

    @Test
    fun testSimpleDataClass() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val ktFileGenerator = JavaFileGenerator()
        val item = JavaDataClassGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )

        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(DataClass("c")).apply {
            addBlockComment("182TEST_COMMENT")
            field("A", DataType.int32,  1)
            field("B", DataType.float64,  0.5f)
            field("C", DataType.stringNullable)
        }

        val projectOutput = OutputTree(Target.Kotlin)
        val files = ktFileGenerator.createFile(projectOutput,
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
        //     <region>
        //        <CommentsBlock>...</CommentsBlock>
        //        <OutBlock>
        //           <OutBlockArguments>
        //              <ArgumentNode><int><A></<ArgumentNode>
        //              <ArgumentNode><int><B></<ArgumentNode>
        //              <ArgumentNode><int><C></<ArgumentNode>
        //           </OutBlockArguments>
        //        </OutBlock>
        //     </region>
        // </FileData>


        Assert.assertTrue("Dirty flag should be true", mainFile.isDirty)
        Assert.assertEquals(4, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[3] is RegionImpl)
        val region = mainFile.subs[3] as Region
        Assert.assertEquals(2, region.subs.size)
        Assert.assertTrue(region.subs[0] is CommentsBlock)
        Assert.assertTrue(region.subs[1] is OutBlock)
        Assert.assertEquals("182TEST_COMMENT", (region.subs[0] as CommentsBlock).subs[0].name)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(1, outBlock.subs.size)
        val outBlockArgs = outBlock.findOrNull(OutBlockArguments::class.java)!!
        Assert.assertEquals(3, outBlockArgs.subs.size)

        // check A rvalue
        val aArgument = outBlockArgs.subs[0] as ArgumentNode
        Assert.assertEquals(2, aArgument.subs.size)

        // check C rvalue
        val cArgument = outBlockArgs.subs[2] as ArgumentNode
        Assert.assertEquals(2, cArgument.subs.size)
    }
}