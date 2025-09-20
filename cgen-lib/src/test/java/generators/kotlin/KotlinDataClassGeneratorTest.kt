package generators.kotlin

import ce.defs.DataType
import ce.defs.RValue
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
import generators.obj.syntaxParseTree.AstTypeLeaf
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.Constructor
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.VariableName
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinDataClassGeneratorTest {
    val codeStyle = CodeStyle(
        newLinesBeforeClass = 1,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val repo = CLikeCodestyleRepo(codeStyle)
    val ktFileGenerator = KotlinFileGenerator()
    val prepareRightValueUseCase = PrepareRightValueUseCase(
        getTypeNameUseCase = getTypeNameUseCase
    )
    val ktDataClassGenerator = KtDataClassGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
        dataTypeToString = getTypeNameUseCase,
        prepareRightValueUseCase = prepareRightValueUseCase
    )

    @Test
    fun testSimpleDataClass() {
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(DataClass("c")).apply {
            addBlockComment("182TEST_COMMENT")
            field("A", DataType.int32,  1)
            field("B", DataType.float64,  0.5f)
            field("C", DataType.stringNullable)
        }

        val projectOutput = OutputTree(Target.Kotlin)
        val files = ktFileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        ktDataClassGenerator(files, block)

        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //     <region>
        //        <CommentsBlock>...</CommentsBlock>
        //        <OutBlock>
        //           <OutBlockArguments>
        //              <ArgumentNode><val><A><:><int><=><RValue 1></<ArgumentNode>
        //              <ArgumentNode><val><B><:><float><=><RValue 0.5f></<ArgumentNode>
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
        Assert.assertTrue(aArgument.subs[0] is Keyword)
        Assert.assertEquals("val", aArgument.subs[0].name)
        Assert.assertTrue(aArgument.subs[1] is VariableName)
        Assert.assertTrue(aArgument.subs[2] is Keyword)
        Assert.assertTrue(aArgument.subs[3] is AstTypeLeaf)
        Assert.assertTrue(aArgument.subs[4] is Keyword)
        Assert.assertTrue(aArgument.subs[5] is RValue)
        Assert.assertEquals("1", aArgument.subs[5].name)

        // check C rvalue
        val cArgument = outBlockArgs.subs[2] as ArgumentNode
        Assert.assertEquals(4, cArgument.subs.size)
    }

    @Test
    fun testDataClassWithSelfStaticInstance() {
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val dataClassDescriptor = DataClass("MyDataClass").apply {
            field("A", DataType.int32,  1)
            field("B", DataType.float64,  0.5f)
            addstaticfield("SELF", DataType.custom(this), instance(
                mapOf("A" to 10, "B" to 10.5f)
            ))
        }
        val block = namespace.addSub(dataClassDescriptor)

        val projectOutput = OutputTree(Target.Kotlin)
        val files = ktFileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        ktDataClassGenerator(files, block)

        // expected result
        // <FileData>
        //     <NamespaceDeclaration />
        //     <ImportsBlock />
        //     <region>
        //        <OutBlock data class>
        //           <OutBlockArguments>
        //              <ArgumentNode><val><A><:><int><=><1></<ArgumentNode>
        //              <ArgumentNode><val><B><:><float><=><0.5f></<ArgumentNode>
        //           </OutBlockArguments>
        //           <OutBlock companion object>
        //              <FieldNode><val><SELF><:><c><=>
        //                  <DataValue>
        //                      <Constructor c>
        //                          <Arguments>
        //                              <ArgumentNode><A><=><RValue 10></<ArgumentNode>
        //                              <ArgumentNode><B><=><RValue 10.5f></<ArgumentNode>
        //                          </Arguments>
        //                      </Constructor>
        //                  </Datavalue>
        //              </FieldNode>
        //           </OutBlock companion object>
        //        </OutBlock data class>
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
        val outBlockArgs = outBlock.findOrNull(OutBlockArguments::class.java)!!
        Assert.assertEquals(2, outBlockArgs.subs.size)
        val companionObject = outBlock.subs[1] as OutBlock
        Assert.assertEquals(1, companionObject.subs.size)
        val companionObjectField = companionObject.subs[0] as FieldNode
        Assert.assertEquals(6, companionObjectField.subs.size)
        Assert.assertTrue(companionObjectField.subs[0] is Keyword)
        Assert.assertTrue(companionObjectField.subs[1] is VariableName)
        Assert.assertTrue(companionObjectField.subs[2] is Keyword)
        Assert.assertTrue(companionObjectField.subs[3] is AstTypeLeaf)
        Assert.assertTrue(companionObjectField.subs[4] is Keyword)
        Assert.assertTrue(companionObjectField.subs[5] is RValue)
        val dataValue = companionObjectField.subs[5] as RValue
        Assert.assertEquals(1, dataValue.subs.size)
        Assert.assertTrue(dataValue.subs[0] is Constructor)
        val constructor = dataValue.subs[0] as Constructor
        Assert.assertEquals(1, constructor.subs.size)
    }
}