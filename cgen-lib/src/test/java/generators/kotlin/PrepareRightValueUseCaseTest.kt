package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.input.NewInstance
import generators.obj.input.setValue
import generators.obj.out.FileDataImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PrepareRightValueUseCaseTest {
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val dataTypeToString = GetTypeNameUseCase(arrayDataType)
    private val fileData = FileDataImpl(name = "testFile")
    private val prepareRightValueUseCase = PrepareRightValueUseCase(dataTypeToString)

    @Test
    fun toRightValueReturnsVoidForVoidType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.VOID, DataValue("ignored"), fileData)
        assertEquals("void", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForIntegerTypes() {
        val result = prepareRightValueUseCase.toRightValue(DataType.int32, DataValue(simple = 42), fileData)
        assertEquals("42", result.name)
    }

    @Test
    fun toRightValueAppendsFForFloat32Type() {
        val result = prepareRightValueUseCase.toRightValue(DataType.float32, DataValue(simple = 3.14), fileData)
        assertEquals("3.14f", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForFloat64Type() {
        val result = prepareRightValueUseCase.toRightValue(DataType.float64, DataValue(simple = 3.14), fileData)
        assertEquals("3.14", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForBooleanType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.bool, DataValue(simple = true), fileData)
        assertEquals("true", result.name)
    }

    @Test
    fun toRightValueReturnsSimpleValueForStringType() {
        val result = prepareRightValueUseCase.toRightValue(DataType.string(), DataValue(simple = "hello"), fileData)
        assertEquals("hello", result.name)
    }

    @Test
    fun toRightValueCustomClass() {
        val dataClassDescriptor = DataClass("c").apply {
            field("A", DataType.int32,  1)
        }
        val dataType = DataType.custom(dataClassDescriptor)
        val instance = NewInstance("newInstance", type = dataType)
        instance.argument("a", DataType.int32, NotDefined)
        instance.argument("b", DataType.int32, 1)
        val field = DataField("name", dataType).setValue(instance)

        val result = prepareRightValueUseCase.toRightValue(field.type, field.value, fileData)
        assertEquals("newInstance", result.name)
        assertEquals(NewInstance::class, result::class)
    }

}