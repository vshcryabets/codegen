package generators.kotlin

import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.settings.CodeStyle
import ce.treeio.XmlTreeReader
import generators.obj.input.ConstantsBlock
import generators.obj.input.NamespaceImpl
import generators.obj.input.findOrNull
import generators.obj.out.FieldNode
import generators.obj.out.OutBlock
import generators.obj.out.OutputTree
import generators.obj.out.Region
import generators.obj.out.RegionImpl
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinConstantsGeneratorTest {
    private val reader = XmlTreeReader()
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    private val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val repo = CLikeCodestyleRepo(codeStyle)
    private val fileGenerator = KotlinFileGenerator()
    private val prepareRightValueUseCase = PrepareRightValueUseCase(getTypeNameUseCase)
    private val item = KtConstantsGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
        dataTypeToString = getTypeNameUseCase,
        prepareRightValueUseCase = prepareRightValueUseCase
    )

    @Test
    fun testConstantsClass() {
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

        val projectOutput = OutputTree(Target.Kotlin)
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        item(files, block)

        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //     <region>
        //        <OutBlock>
        //          <FieldNode>
        //              <Keyword const />
        //              <Keyword val />
        //              <VariableName OREAD />
        //              <Keyword : />
        //              <AstTypeLeaf Int />
        //              <Keyword = />
        //              <RValue 0 />
        //          </FieldNode>
        //          <FieldNode ... />
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
        val node1 = outBlock.subs[0] as FieldNode
        Assert.assertEquals(7, node1.subs.size)
        Assert.assertEquals("const", node1.subs[0].name)
        Assert.assertEquals("val", node1.subs[1].name)
        Assert.assertEquals("OREAD", node1.subs[2].name)
        Assert.assertEquals(":", node1.subs[3].name)
        Assert.assertEquals("Int", node1.subs[4].name)
        Assert.assertEquals("=", node1.subs[5].name)
        Assert.assertEquals("0", node1.subs[6].name)

        // check OWRITE node
        val node2 = outBlock.subs[1] as FieldNode
        Assert.assertEquals(7, node2.subs.size)
    }

    @Test
    fun testStringConstants() {
        val tree = reader.loadFromString("""
            <Namespace name="com.goldman.xml">
                <ConstantsBlock defaultType="string" name="Constants">
                    <ConstantDesc name="Const1" value="ABC"/>
                    <ConstantDesc name="Const2" value="DEF"/>
                </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        val lastNs = (tree as NamespaceImpl).getNamespace("goldman.xml")
        val block = lastNs.subs.first() as ConstantsBlock

        val projectOutput = OutputTree(Target.Kotlin)
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        item(files, block)
        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //     <region>
        //        <OutBlock>
        //          <FieldNode>
        //              <Keyword const />
        //              <Keyword val />
        //              <VariableName Const1 />
        //              <Keyword : />
        //              <AstTypeLeaf String />
        //              <Keyword = />
        //              <RValue "ABC" />
        //          </FieldNode>
        //          <FieldNode ... />
        //        </OutBlock>
        //     </region>
        // </FileData>

        Assert.assertTrue("Dirty flag should be true", mainFile.isDirty)
        Assert.assertEquals(3, mainFile.subs.size)
        Assert.assertTrue(mainFile.subs[2] is RegionImpl)
        val region = mainFile.subs[2] as Region
        Assert.assertEquals(1, region.subs.size)
        Assert.assertTrue(region.subs[0] is OutBlock)
        val outBlock = region.subs[0] as OutBlock
        Assert.assertEquals(2, outBlock.subs.size)
        // check Const1 node
        val node1 = outBlock.subs[0] as FieldNode
        Assert.assertEquals(7, node1.subs.size)
        Assert.assertEquals("const", node1.subs[0].name)
        Assert.assertEquals("val", node1.subs[1].name)
        Assert.assertEquals("Const1", node1.subs[2].name)
        Assert.assertEquals(":", node1.subs[3].name)
        Assert.assertEquals("String", node1.subs[4].name)
        Assert.assertEquals("=", node1.subs[5].name)
        Assert.assertEquals("\"ABC\"", node1.subs[6].name)
    }
}