package ce.formatters.kotlin

import ce.defs.DataType
import ce.defs.RValue
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.settings.CodeStyle
import generators.kotlin.GetArrayDataTypeUseCase
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KtDataClassGenerator
import generators.kotlin.PrepareRightValueUseCase
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.TreeRoot
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addRValue
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.Constructor
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.Indent
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.Separator
import generators.obj.syntaxParseTree.Space
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinDataClassFormattingTests {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val formatter = CodeFormatterKotlinUseCaseImpl(repoNoSpace)
    val ktFileGenerator = KotlinFileGenerator()
    val prepareRightValueUseCase = PrepareRightValueUseCase(
        getTypeNameUseCase = getTypeNameUseCase
    )
    val ktDataClassGenerator = KtDataClassGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repoNoSpace),
        dataTypeToString = getTypeNameUseCase,
        prepareRightValueUseCase = prepareRightValueUseCase
    )


    @Test
    fun testDataClassOneArgumentsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("data class TEST") {
                addSub(OutBlockArguments()).apply {
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("A")
                        addKeyword(":")
                        addDatatype("int")
                        addKeyword("=")
                        addRValue("1")
                    })
                }
            }
        }
        val output = formatter(input)
        // expected result
        // <Region>
        //     <OutBlock>
        //        <(>
        //        <OutBlockArguments>
        //          <ArgumentNode>
        //              <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //          </ArgumentNode>
        //        </OutBLockArguments>
        //        <)>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[2] is Keyword)
        Assert.assertEquals(3, outBlock.subs.size)

        val outBlockArguments = outBlock.subs[1] as OutBlockArguments
        Assert.assertEquals(1, outBlockArguments.subs.size)

        Assert.assertTrue(outBlockArguments.subs[0] is ArgumentNode)
        val argumentNode = outBlockArguments.subs[0] as ArgumentNode
        Assert.assertTrue(argumentNode.subs[0] is Keyword)
        Assert.assertEquals(10, argumentNode.subs.size)
    }

    @Test
    fun testDataClassMultimpleArgumentsFormatting() {
        val input = RegionImpl().apply {
            addOutBlock("data class TEST") {
                addSub(OutBlockArguments()).apply {
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("A")
                        addKeyword(":")
                        addDatatype("int")
                        addKeyword("=")
                        addRValue("1")
                    })
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("B")
                        addKeyword(":")
                        addDatatype("float")
                        addKeyword("=")
                        addRValue("0.5f")
                    })
                    addSub(ArgumentNode().apply {
                        addKeyword("val")
                        addVarName("V")
                        addKeyword(":")
                        addDatatype("String?")
                    })
                }
            }
        }
        val output = formatter(input)

        // expected result
        // <Region>
        //     <OutBlock>
        //        <(> <NL>
        //        <OutBlockArguments>
        //          <TAB>
        //          <ArgumentNode>
        //              <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //          </ArgumentNode><,><NL>
        //          <TAB>
        //          <ArgumentNode>
        //              <val><SPACE><B><:><SPACE><float><SPACE><=><SPACE><0.5f>
        //          </ArgumentNode><,><NL>
        //          <TAB>
        //          <ArgumentNode>
        //              <val><SPACE><C><:><SPACE><String?><NL>
        //          </ArgumentNode>
        //        </OutBLockArguments><NL>
        //        <)>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(5, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is NlSeparator)
        Assert.assertTrue(outBlock.subs[2] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[3] is NlSeparator)
        Assert.assertTrue(outBlock.subs[4] is Keyword)

        val outBlockArguments = outBlock.subs[2] as OutBlockArguments
        Assert.assertEquals(10, outBlockArguments.subs.size)

        Assert.assertTrue(outBlockArguments.subs[0] is Indent)
        Assert.assertTrue(outBlockArguments.subs[1] is ArgumentNode)
        Assert.assertTrue(outBlockArguments.subs[2] is Separator)
        Assert.assertTrue(outBlockArguments.subs[3] is NlSeparator)
        Assert.assertTrue(outBlockArguments.subs[4] is Indent)
        Assert.assertTrue(outBlockArguments.subs[5] is ArgumentNode)
        Assert.assertTrue(outBlockArguments.subs[6] is Separator)
        Assert.assertTrue(outBlockArguments.subs[7] is NlSeparator)
        Assert.assertTrue(outBlockArguments.subs[8] is Indent)
        Assert.assertTrue(outBlockArguments.subs[9] is ArgumentNode)

        val argumentNode = outBlockArguments.subs[1] as ArgumentNode
        Assert.assertTrue(argumentNode.subs[0] is Keyword)
        Assert.assertEquals(10, argumentNode.subs.size)
    }

    @Test
    fun testDataClassWithInstance() {
        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val dataClassDescriptor = DataClass("MyDataClass").apply {
            field("A", DataType.int32,  1)
            field("B", DataType.float64,  0.5f)
            addstaticfield("SELF", DataType.custom(this), instance())
            //                 mapOf("A" to 10, "B" to 10.5f)
        }
        val block = namespace.addSub(dataClassDescriptor)

        val projectOutput = OutputTree(Target.Kotlin)
        val files = ktFileGenerator.createFile(projectOutput,
            workingDirectory = "./",
            packageDirectory = "",
            "a", block)
        val mainFile = files.first()
        ktDataClassGenerator(files, block)
        val region = mainFile.subs[3] as Region
        val output = formatter(region)

        // expected result
        // <Region>
        //     <OutBlock data class MyDataClass>
        //        <(> <NL>
        //        <OutBlockArguments>
        //          <Indent>
        //          <ArgumentNode>
        //              <val><SPACE><A><:><SPACE><int><SPACE><=><SPACE><1>
        //          </ArgumentNode><,><NL>
        //          <Indent>
        //          <ArgumentNode>
        //              <val><SPACE><B><:><SPACE><float><SPACE><=><SPACE><0.5f>
        //          </ArgumentNode>
        //        </OutBLockArguments>
        //        <NL> <)><SPACE> <{> <nl>
        //        <Indent>
        //        <OutBlock companion object>
        //             <SPACE><{><NL>
        //             <Indent><Indent>
        //             <FieldNode>
        //                 <val><SPACE><SELF><:><SPACE><MyDataClass><SPACE><=><SPACE>
        //                 <RValue>
        //                      <Constructor MyDataClass>
        //                          <(><Arguments /><)>
        //                      </Constructor>
        //                 </RValue>
        //             </FieldNode>
        //             <NL>
        //             <}>
        //        </OutBlock companion object>
        //        <NL>
        //        <}>
        //     </OutBlock>
        //     <NL>
        // </Region>
        Assert.assertEquals(2, output.subs.size)
        val outBlock = output.subs[0] as OutBlock
        Assert.assertEquals(12, outBlock.subs.size)
        Assert.assertTrue(outBlock.subs[0] is Keyword)
        Assert.assertTrue(outBlock.subs[1] is NlSeparator)
        Assert.assertTrue(outBlock.subs[2] is OutBlockArguments)
        Assert.assertTrue(outBlock.subs[3] is NlSeparator)
        Assert.assertTrue(outBlock.subs[4] is Keyword)
        Assert.assertTrue(outBlock.subs[5] is Space)
        Assert.assertTrue(outBlock.subs[6] is Keyword)
        Assert.assertTrue(outBlock.subs[7] is NlSeparator)
        Assert.assertTrue(outBlock.subs[8] is Indent)
        Assert.assertTrue(outBlock.subs[9] is OutBlock)
        Assert.assertTrue(outBlock.subs[10] is NlSeparator)
        Assert.assertTrue(outBlock.subs[11] is Keyword)

        // check out block arguments
        val outBlockArguments = outBlock.subs[2] as OutBlockArguments
        Assert.assertEquals(6, outBlockArguments.subs.size)

        Assert.assertTrue(outBlockArguments.subs[0] is Indent)
        Assert.assertTrue(outBlockArguments.subs[1] is ArgumentNode)
        Assert.assertTrue(outBlockArguments.subs[2] is Separator)
        Assert.assertTrue(outBlockArguments.subs[3] is NlSeparator)
        Assert.assertTrue(outBlockArguments.subs[4] is Indent)
        Assert.assertTrue(outBlockArguments.subs[5] is ArgumentNode)

        val argumentNode = outBlockArguments.subs[1] as ArgumentNode
        Assert.assertTrue(argumentNode.subs[0] is Keyword)
        Assert.assertEquals(10, argumentNode.subs.size)

        // check companion object
        val companionObject = outBlock.subs[9] as OutBlock
        Assert.assertEquals(8, companionObject.subs.size)
        val fieldNode = companionObject.subs[5] as FieldNode
        Assert.assertEquals(10, fieldNode.subs.size)
        Assert.assertEquals(RValue::class, fieldNode.subs[9]::class)
        val rValue = fieldNode.subs[9] as RValue
        Assert.assertEquals(1, rValue.subs.size)
        Assert.assertTrue(rValue.subs[0] is Constructor)
        val constructor = rValue.subs[0] as Constructor
        Assert.assertEquals("MyDataClass", constructor.name)
        Assert.assertEquals(3, constructor.subs.size)
    }
}