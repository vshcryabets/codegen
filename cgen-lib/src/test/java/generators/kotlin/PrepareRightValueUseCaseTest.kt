package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.defs.NotDefined
import ce.defs.RValue
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.input.NewInstance
import generators.obj.input.setType
import generators.obj.input.setValue
import generators.obj.out.FileDataImpl
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
        val result = prepareRightValueUseCase.toRightValue(DataType.VOID, DataValueImpl("ignored"), fileData)
        assertEquals("void", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForIntegerTypes() {
        val result = prepareRightValueUseCase.toRightValue(DataType.int32, DataValueImpl(simple = 42), fileData)
        assertEquals("42", result.name)
    }

    @Test
    fun toRightValueAppendsFForFloat32Type() {
        val result = prepareRightValueUseCase.toRightValue(DataType.float32, DataValueImpl(simple = 3.14), fileData)
        assertEquals("3.14f", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForFloat64Type() {
        val result = prepareRightValueUseCase.toRightValue(DataType.float64, DataValueImpl(simple = 3.14), fileData)
        assertEquals("3.14", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForBooleanType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.bool, DataValueImpl(simple = true), fileData)
        assertEquals("true", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForStringType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.string(), DataValueImpl(simple = "hello"), fileData)
        assertEquals("hello", result.name)
    }

    @Test
    fun toRightValueCustomClass() {
        val dataClassDescriptor = DataClass("c").apply {
            field("A", DataType.int32,  1)
        }
        val dataType = DataType.custom(dataClassDescriptor)
        val instance = NewInstance("newInstance").setType(type = dataType)
        instance.argument("a", DataType.int32, NotDefined)
        instance.argument("b", DataType.int32, 1)
        val field = DataField("name").setValue(instance).setType(dataType)

        val result = prepareRightValueUseCase.toRightValue(field, fileData)
        assertTrue(result is RValue)
    }

    @Test
    fun testConstructorToAst() {
        val className = "className"
        val dataClassDescriptor = DataClass(className).apply {
            field("A", DataType.int32,  1)
        }
        val dataType = DataType.custom(dataClassDescriptor)
        val instance = NewInstance("newInstance")
            .setType(dataType)
            .argument("a", DataType.int32, NotDefined)
            .argument("b", DataType.int32, 1)

        val result = prepareRightValueUseCase.prepareConstructor(instance, fileData)
        assertTrue(result is RValue)
        assertEquals(1, result.subs.size)
        val newInstanceNode = result.subs[0]
        assertTrue(newInstanceNode is NewInstance)

    }

}