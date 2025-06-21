package ce.writters

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterKotlinUseCaseImpl
import ce.io.CodeWritter
import ce.settings.CodeStyle
import generators.kotlin.GetArrayDataTypeUseCase
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinFileGenerator
import generators.kotlin.KotlinWritter
import generators.kotlin.PrepareRightValueUseCase
import generators.obj.input.ConstantsEnum
import generators.obj.input.NamespaceImpl
import generators.obj.input.addKeyword
import generators.obj.input.addRValue
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.AstTypeLeaf
import generators.obj.out.EnumNode
import generators.obj.out.FieldNode
import generators.obj.out.OutBlock
import generators.obj.out.OutputTree
import generators.obj.out.Region
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinWritterTest {
    private val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    private val fileGenerator = KotlinFileGenerator()
    private val writter = KotlinWritter(repoNoSpace, "")
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)
    private val prepareRightValueUseCase = PrepareRightValueUseCase(dataTypeToString)
    private val formatter = CodeFormatterKotlinUseCaseImpl(repoNoSpace)

    @Test
    fun testConstantNodeWithSimpleRvalue() {
        var buffer = StringBuffer()
        val input = FieldNode().apply {
            addKeyword("const")
            addSeparator(" ")
            addKeyword("val")
            addSeparator(" ")
            addVarName("OREAD")
            addKeyword(":")
            addSeparator(" ")
            addSub(AstTypeLeaf("Int"))
            addSeparator(" ")
            addKeyword("=")
            addSeparator(" ")
            addRValue("0")
        }
        writter.writeNode(input, object : CodeWritter {
            override fun write(str: String): CodeWritter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWritter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWritter = this
            override fun setIndent(str: String): CodeWritter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("const val OREAD: Int = 0", buffer.toString())
    }

    @Test
    fun testEnumNodeWithSimpleRvalue() {
        val tree = NamespaceImpl("data").apply {
            addSub(ConstantsEnum("CryptoCurrency")).apply {
                defaultType(DataType.int16)
                add("OK", 0)
            }
        }
        val block = tree.subs.first() as ConstantsEnum
        val projectOutput = OutputTree(Target.Kotlin)
        val files = fileGenerator.createFile(projectOutput, "a", block)
        val mainFile = files.first()
        val process = KotlinEnumGenerator(
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repoNoSpace),
            dataTypeToString = dataTypeToString,
            prepareRightValueUseCase = prepareRightValueUseCase
        )
        process(files, block)
        val inputEnumNode = ((mainFile.subs[2] as Region).subs[0] as OutBlock).subs[1] as EnumNode
        val formatedNode = formatter(inputEnumNode)
        val buffer = StringBuffer()

        writter.writeNode(formatedNode, object : CodeWritter {
            override fun write(str: String): CodeWritter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWritter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWritter = this
            override fun setIndent(str: String): CodeWritter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("OK(0)", buffer.toString())
    }

}