package generators.kotlin

import ce.defs.DataType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetArrayDataTypeUseCaseTest {
    private val useCase = GetArrayDataTypeUseCase()
    @Test
    fun getArrayTypeReturnsByteArrayForInt8() {
        val result = useCase.getArrayType(DataType.int8)
        assertEquals("ByteArray", result)
    }

    @Test
    fun getArrayTypeReturnsByteArrayForUint8() {
        val result = useCase.getArrayType(DataType.uint8)
        assertEquals("ByteArray", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForInt16() {
        val result = useCase.getArrayType(DataType.int16)
        assertEquals("ShortArray", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForInt32() {
        val result = useCase.getArrayType(DataType.int32)
        assertEquals("IntArray", result)
    }

    @Test
    fun getArrayTypeReturnsLongArrayForInt64() {
        val result = useCase.getArrayType(DataType.int64)
        assertEquals("LongArray", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForUint16() {
        val result = useCase.getArrayType(DataType.uint16)
        assertEquals("ShortArray", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForUint32() {
        val result = useCase.getArrayType(DataType.uint32)
        assertEquals("IntArray", result)
    }

    @Test
    fun getArrayTypeReturnsFloatArrayForFloat32() {
        val result = useCase.getArrayType(DataType.float32)
        assertEquals("FloatArray", result)
    }

    @Test
    fun getArrayTypeReturnsDoubleArrayForFloat64() {
        val result = useCase.getArrayType(DataType.float64)
        assertEquals("DoubleArray", result)
    }

    @Test
    fun getArrayTypeReturnsStringArrayForStringType() {
        val result = useCase.getArrayType(DataType.string())
        assertEquals("String[]", result)
    }

    @Test
    fun getArrayTypeReturnsUserClassPathForUserClassType() {
        val userClassType = DataType.userClass("com.example.MyClass")
        val result = useCase.getArrayType(userClassType)
        assertEquals("Array<com.example.MyClass>", result)
    }
}