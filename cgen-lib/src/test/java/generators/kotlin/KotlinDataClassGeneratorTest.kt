package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Test

class KotlinDataClassGeneratorTest {

    @Test
    fun testSimpleDataClass() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val ktFileGenerator = KotlinFileGenerator()
        val item = KtDataClassGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )

        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(DataClass("c")).apply {
            addBlockComment("182TEST_COMMENT")
            field("A", DataType.int32,  1)
            field("B", DataType.float64,  0.5f)
            field("C", DataType.string(true))
        }

        val projectOutput = ProjectOutput(Target.Kotlin)
        val files = ktFileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        item(files, block)

        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //     <region>
        //        <CommentsBlock>...</CommentsBlock>
        //        <OutBlock>
        //           <OutBlockArguments>
        //              <ArgumentNode><val><A><:><int><=><1></<ArgumentNode>
        //              <ArgumentNode><val><B><:><float><=><0.5f></<ArgumentNode>
        //              <ArgumentNode><val><C><:><string></<ArgumentNode>
        //           </OutBlockArguments>
        //        </OutBlock>
        //     </region>
        // </FileData>


        Assert.assertTrue("Dirty flag should be true", mainFile.isDirty)
        Assert.assertEquals(3, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[2] is RegionImpl)
        val region = mainFile.subs[2] as Region
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
        Assert.assertEquals(6, aArgument.subs.size)
        Assert.assertEquals("1", aArgument.subs[5].name)

        // check C rvalue
        val cArgument = outBlockArgs.subs[2] as ArgumentNode
        Assert.assertEquals(4, cArgument.subs.size)
    }
}