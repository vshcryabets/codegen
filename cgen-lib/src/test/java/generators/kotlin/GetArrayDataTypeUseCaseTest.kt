package generators.kotlin

import ce.defs.DataType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GetArrayDataTypeUseCaseTest {
    @Test
    fun getArrayTypeReturnsByteArrayForInt8() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.int8)
        assertEquals("ByteArray", result)
    }

    @Test
    fun getArrayTypeReturnsByteArrayForUint8() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.uint8)
        assertEquals("ByteArray", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForInt16() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.int16)
        assertEquals("ShortArray", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForInt32() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.int32)
        assertEquals("IntArray", result)
    }

    @Test
    fun getArrayTypeReturnsLongArrayForInt64() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.int64)
        assertEquals("LongArray", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForUint16() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.uint16)
        assertEquals("ShortArray", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForUint32() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.uint32)
        assertEquals("IntArray", result)
    }

    @Test
    fun getArrayTypeReturnsFloatArrayForFloat32() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.float32)
        assertEquals("FloatArray", result)
    }

    @Test
    fun getArrayTypeReturnsDoubleArrayForFloat64() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.float64)
        assertEquals("DoubleArray", result)
    }

    @Test
    fun getArrayTypeReturnsStringArrayForStringType() {
        val result = GetArrayDataTypeUseCase().getArrayType(DataType.string())
        assertEquals("String[]", result)
    }

    @Test
    fun getArrayTypeReturnsUserClassPathForUserClassType() {
        val userClassType = DataType.userClass("com.example.MyClass")
        val result = GetArrayDataTypeUseCase().getArrayType(userClassType)
        assertEquals("Arryay<com.example.MyClass>", result)
    }
}