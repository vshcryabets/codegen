package generators.java

import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.input.*
import generators.obj.out.*
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class JavaConstantsGeneratorTest {
    private val reader = XmlTreeReader()

    @Test
    fun testConstantsClass() {
        val codeStyle = CodeStyle(
            newLinesBeforeClass = 1,
            tabSize = 2,
            preventEmptyBlocks = true,
        )
        val repo = CLikeCodestyleRepo(codeStyle)
        val fileGenerator = JavaFileGenerator()
        val item = JavaConstantsGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo)
        )

        val tree = reader.loadFromString("""
            <Namespace name="com.goldman.xml">
                    <ConstantsBlock defaultType="int32" name="Constants">
                        <ConstantDesc name="OREAD" value="0"/>
                        <ConstantDesc name="OWRITE" value="1"/>
                    </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        val lastNs = (tree as NamespaceImpl).getNamespace("goldman.xml")
        val block = lastNs.subs.first() as ConstantsBlock

        val projectOutput = OutputTree(Target.Java)
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        item(files, block)

        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //     <region>
        //        <CommentsBlock>...</CommentsBlock>
        //        <OutBlock>

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