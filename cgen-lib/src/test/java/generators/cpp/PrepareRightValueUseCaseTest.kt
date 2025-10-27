package generators.cpp

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.defs.NotDefined
import ce.defs.RValue
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.NewInstance
import generators.obj.abstractSyntaxTree.findOrCreateSub
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.Constructor
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.ImportsBlock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PrepareRightValueUseCaseTest {
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)
    private val fileData = FileDataImpl(name = "testFile")
    private val importsBlock = fileData.findOrCreateSub(ImportsBlock::class.java)
    private val prepareRightValueUseCase = PrepareRightValueUseCase(dataTypeToString)

    @Test
    fun toRightValueReturnsVoidForVoidType() {
        val result = prepareRightValueUseCase.toRightValue(
            DataType.VOID,
            DataValueImpl("ignored"), importsBlock
        )
        assertEquals("void", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForIntegerTypes() {
        val result = prepareRightValueUseCase.toRightValue(
            DataType.int32,
            DataValueImpl(simple = 42), importsBlock
        )
        assertEquals("42", result.name)
    }

    @Test
    fun toRightValueAppendsFForFloat32Type() {
        val result = prepareRightValueUseCase.toRightValue(
            DataType.float32,
            DataValueImpl(simple = 3.14), importsBlock
        )
        assertEquals("3.14f", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForFloat64Type() {
        val result = prepareRightValueUseCase.toRightValue(
            DataType.float64,
            DataValueImpl(simple = 3.14), importsBlock
        )
        assertEquals("3.14", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForBooleanType() {
        val result = prepareRightValueUseCase.toRightValue(
            DataType.bool,
            DataValueImpl(simple = true), importsBlock
        )
        assertEquals("true", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForStringType() {
        val result = prepareRightValueUseCase.toRightValue(
            DataType.string,
            DataValueImpl(simple = "hello"), importsBlock
        )
        assertEquals("\"hello\"", result.name)
    }

    @Test
    fun toRightValueCustomClass() {
        val dataClassDescriptor = DataClass("c").apply {
            field("A", DataType.int32, 1)
        }
        val dataType = DataType.custom(dataClassDescriptor)
        val instance = NewInstance("newInstance")
        instance.setType(type = dataType)
        instance.argument("a", DataType.int32, NotDefined)
        instance.argument("b", DataType.int32, 1)
        val field = DataField("name").apply {
            setValue(instance)
            setType(dataType)
        }

        val result = prepareRightValueUseCase.toRightValue(
            dataField = field,
            importsBlock = importsBlock
        )
        assertTrue(result is RValue)
    }

    @Test
    fun testConstructorToAst() {
        val className = "className"
        val dataClassDescriptor = DataClass(className).apply {
            field("a", DataType.string, "")
            field("b", DataType.int32, 0)
        }
        val dataType = DataType.custom(dataClassDescriptor)
        val instance = dataClassDescriptor.instance(
            mapOf("a" to "defined", "b" to 123)
        )

        val result = prepareRightValueUseCase.prepareConstructor(instance, importsBlock)
        // expected result
        // <RValue>
        //   <Constructor>
        //     <Arguments>
        //       <ArgumentNode>
        //          <RValue "defined">
        //       </ArgumentNode>
        //       <ArgumentNode>
        //          <RValue 123>
        //       </ArgumentNode>
        //     </Arguments>
        //   </Constructor>
        // </RValue>
        assertTrue(result is RValue)
        assertEquals(1, result.subs.size)
        assertTrue(result.subs[0] is Constructor)
        val constructor = result.subs[0] as Constructor
        Assertions.assertEquals(1, constructor.subs.size)
        assertTrue(constructor.subs[0] is Arguments)
        val arguments = constructor.subs[0] as Arguments
        assertEquals(2, arguments.subs.size)
        val arg1 = arguments.subs[0] as ArgumentNode
        val arg2 = arguments.subs[1] as ArgumentNode
        assertEquals(1, arg1.subs.size)
        assertEquals(RValue::class.java, arg1.subs[0].javaClass)
        val rvalue1 = arg1.subs[0] as RValue
        assertEquals("\"defined\"", rvalue1.name)
        assertEquals("123", arg2.subs[0].name)
    }
}