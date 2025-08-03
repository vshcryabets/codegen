package ce.writers.kotlin

import ce.basetest.KotlinBaseTest
import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.io.CodeWriter
import generators.kotlin.GetTypeNameUseCase
import generators.kotlin.KotlinEnumGenerator
import generators.kotlin.KotlinWriter
import generators.obj.input.ConstantsEnum
import generators.obj.input.NamespaceImpl
import generators.obj.input.addKeyword
import generators.obj.input.addRValue
import generators.obj.input.addSeparator
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.Arguments
import generators.obj.out.AstTypeLeaf
import generators.obj.out.Constructor
import generators.obj.out.EnumNode
import generators.obj.out.FieldNode
import generators.obj.out.OutBlock
import generators.obj.out.OutputTree
import generators.obj.out.Region
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class KotlinWriterTest: KotlinBaseTest() {
    private val writer = KotlinWriter(repo, "")
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)

    @Test
    fun testConstantNodeWithSimpleRvalue() {
        val buffer = StringBuffer()
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
        writer.writeNode(input, object : CodeWriter {
            override fun write(str: String): CodeWriter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWriter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWriter = this
            override fun setIndent(str: String): CodeWriter = this
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
            addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repo),
            dataTypeToString = dataTypeToString,
            prepareRightValueUseCase = prepareRightValueUseCase
        )
        process(files, block)
        val inputEnumNode = ((mainFile.subs[2] as Region).subs[0] as OutBlock).subs[1] as EnumNode
        val formatedNode = formatter(inputEnumNode)
        val buffer = StringBuffer()

        writer.writeNode(formatedNode, object : CodeWriter {
            override fun write(str: String): CodeWriter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWriter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWriter = this
            override fun setIndent(str: String): CodeWriter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("OK(0)", buffer.toString())
    }

    @Test
    fun testFieldValueWithEmptyConstructor() {
        val input = FieldNode().apply {
            addKeyword("val")
            addSeparator(" ")
            addVarName("testField")
            addKeyword(":")
            addSeparator(" ")
            addSub(AstTypeLeaf("TestClass"))
            addSeparator(" ")
            addKeyword("=")
            addSeparator(" ")
            addRValue("").addSub(
                Constructor("TestClass").apply {
                    addKeyword("(")
                    addSub(Arguments())
                    addKeyword(")")
                }
            )
        }
        val buffer = StringBuffer()
        writer.writeNode(input, object : CodeWriter {
            override fun write(str: String): CodeWriter {
                buffer.append(str)
                return this
            }

            override fun writeNl(): CodeWriter {
                buffer.append("\n")
                return this
            }

            override fun writeNlIfNotEmpty(): CodeWriter = this
            override fun setIndent(str: String): CodeWriter = this
            override fun setNewLine(str: String) {}
        }, "")
        Assert.assertEquals("val testField: TestClass = TestClass()", buffer.toString())
    }


}