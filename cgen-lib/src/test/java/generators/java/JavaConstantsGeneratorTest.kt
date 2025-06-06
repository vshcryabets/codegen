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
        //        <OutBlock>
        //          <ConstantNode>
        //              <public><static><final><int><OREAD><=><0>
        //          </ConstantNode>
        //          <ConstantNode />
        //        </OutBlock>
        //     </region>
        // </FileData>

        Assert.assertTrue("Dirty flag should be true", mainFile.isDirty)
        Assert.assertEquals(3, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[2] is RegionImpl)
        val region = mainFile.subs[2] as Region
        Assert.assertEquals(1, region.subs.size)
        Assert.assertTrue(region.subs[0] is OutBlock)
        val outBlock = region.findOrNull(OutBlock::class.java)!!
        Assert.assertEquals(2, outBlock.subs.size)

        // check OREAD node
        val node1 = outBlock.subs[0] as ConstantNode
        Assert.assertEquals(7, node1.subs.size)

        // check OWRITE node
        val node2 = outBlock.subs[1] as ConstantNode
        Assert.assertEquals(7, node2.subs.size)
    }
}