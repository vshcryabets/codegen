package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.defs.NotDefined
import ce.defs.RValue
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.NewInstance
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.Constructor
import generators.obj.syntaxParseTree.FileDataImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PrepareRightValueUseCaseTest {
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)
    private val fileData = FileDataImpl(name = "testFile")
    private val prepareRightValueUseCase = PrepareRightValueUseCase(dataTypeToString)

    @Test
    fun toRightValueReturnsVoidForVoidType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.VOID,
            DataValueImpl("ignored"), fileData)
        assertEquals("void", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForIntegerTypes() {
        val result = prepareRightValueUseCase.toRightValue(DataType.int32,
            DataValueImpl(simple = 42), fileData)
        assertEquals("42", result.name)
    }

    @Test
    fun toRightValueAppendsFForFloat32Type() {
        val result = prepareRightValueUseCase.toRightValue(DataType.float32,
            DataValueImpl(simple = 3.14), fileData)
        assertEquals("3.14f", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForFloat64Type() {
        val result = prepareRightValueUseCase.toRightValue(DataType.float64,
            DataValueImpl(simple = 3.14), fileData)
        assertEquals("3.14", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForBooleanType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.bool,
            DataValueImpl(simple = true), fileData)
        assertEquals("true", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForStringType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.string(),
            DataValueImpl(simple = "hello"), fileData)
        assertEquals("\"hello\"", result.name)
    }

    @Test
    fun toRightValueCustomClass() {
        val dataClassDescriptor = DataClass("c").apply {
            field("A", DataType.int32,  1)
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

        val result = prepareRightValueUseCase.toRightValue(dataField = field, fileData = fileData)
        assertTrue(result is RValue)
    }

    @Test
    fun testConstructorToAst() {
        val className = "className"
        val dataClassDescriptor = DataClass(className).apply {
            field("a", DataType.string(),  "")
            field("b", DataType.int32,  0)
        }
        val dataType = DataType.custom(dataClassDescriptor)
        val instance = NewInstance("newInstance")
            .setType(dataType)
            .argument("a", DataType.int32, "defined")
            .argument("b", DataType.int32, 1)

        val result = prepareRightValueUseCase.prepareConstructor(instance, fileData)
        // expected result
        // <RValue>
        //   <Constructor>
        //     <Arguments>
        //       <ArgumentNode>
        //          <VarName a><=><"Defined">
        //       </ArgumentNode>
        //       <,>
        //       <ArgumentNode>
        //          <VarName b><=><1>
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
        assertEquals(3, arguments.subs.size)
    }

}