package generators.cpp

import ce.defs.DataType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetArrayDataTypeUseCaseTest {
    private val useCase = GetArrayDataTypeUseCase()
    @Test
    fun getArrayTypeReturnsByteArrayForInt8() {
        val result = useCase.getArrayType(DataType.int8)
        assertEquals("std::vector<int8_t>", result)
    }

    @Test
    fun getArrayTypeReturnsByteArrayForUint8() {
        val result = useCase.getArrayType(DataType.uint8)
        assertEquals("std::vector<uint8_t>", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForInt16() {
        val result = useCase.getArrayType(DataType.int16)
        assertEquals("std::vector<int16_t>", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForInt32() {
        val result = useCase.getArrayType(DataType.int32)
        assertEquals("std::vector<int32_t>", result)
    }

    @Test
    fun getArrayTypeReturnsLongArrayForInt64() {
        val result = useCase.getArrayType(DataType.int64)
        assertEquals("std::vector<int64_t>", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForUint16() {
        val result = useCase.getArrayType(DataType.uint16)
        assertEquals("std::vector<uint16_t>", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForUint32() {
        val result = useCase.getArrayType(DataType.uint32)
        assertEquals("std::vector<uint32_t>", result)
    }

    @Test
    fun getArrayTypeReturnsFloatArrayForFloat32() {
        val result = useCase.getArrayType(DataType.float32)
        assertEquals("std::vector<float>", result)
    }

    @Test
    fun getArrayTypeReturnsDoubleArrayForFloat64() {
        val result = useCase.getArrayType(DataType.float64)
        assertEquals("std::vector<double>", result)
    }

    @Test
    fun getArrayTypeReturnsStringArrayForStringType() {
        val result = useCase.getArrayType(DataType.string)
        assertEquals("std::vector<std::string>", result)
    }

    @Test
    fun getArrayTypeReturnsUserClassPathForUserClassType() {
        val userClassType = DataType.userClass("MyClass")
        val result = useCase.getArrayType(userClassType)
        assertEquals("std::vector<MyClass>", result)
    }
}