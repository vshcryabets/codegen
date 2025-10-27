package generators.cpp

import ce.defs.DataType
import generators.obj.syntaxParseTree.ImportsBlock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetArrayDataTypeUseCaseTest {
    private val useCase = GetArrayDataTypeUseCase()
    private val importsBlock = ImportsBlock()

    @BeforeEach
    fun setup() {
        importsBlock.subs.clear()
    }

    @Test
    fun getArrayTypeReturnsByteArrayForInt8() {
        val result = useCase.getArrayType(DataType.int8, importsBlock)
        assertEquals("std::vector<int8_t>", result)
    }

    @Test
    fun getArrayTypeReturnsByteArrayForUint8() {
        val result = useCase.getArrayType(DataType.uint8, importsBlock)
        assertEquals("std::vector<uint8_t>", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForInt16() {
        val result = useCase.getArrayType(DataType.int16, importsBlock)
        assertEquals("std::vector<int16_t>", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForInt32() {
        val result = useCase.getArrayType(DataType.int32, importsBlock)
        assertEquals("std::vector<int32_t>", result)
    }

    @Test
    fun getArrayTypeReturnsLongArrayForInt64() {
        val result = useCase.getArrayType(DataType.int64, importsBlock)
        assertEquals("std::vector<int64_t>", result)
    }

    @Test
    fun getArrayTypeReturnsShortArrayForUint16() {
        val result = useCase.getArrayType(DataType.uint16, importsBlock)
        assertEquals("std::vector<uint16_t>", result)
    }

    @Test
    fun getArrayTypeReturnsIntArrayForUint32() {
        val result = useCase.getArrayType(DataType.uint32, importsBlock)
        assertEquals("std::vector<uint32_t>", result)
    }

    @Test
    fun getArrayTypeReturnsFloatArrayForFloat32() {
        val result = useCase.getArrayType(DataType.float32, importsBlock)
        assertEquals("std::vector<float>", result)
    }

    @Test
    fun getArrayTypeReturnsDoubleArrayForFloat64() {
        val result = useCase.getArrayType(DataType.float64, importsBlock)
        assertEquals("std::vector<double>", result)
    }

    @Test
    fun getArrayTypeReturnsStringArrayForStringType() {
        val result = useCase.getArrayType(DataType.string, importsBlock)
        assertEquals("std::vector<std::string>", result)
    }

    @Test
    fun getArrayTypeReturnsUserClassPathForUserClassType() {
        val userClassType = DataType.userClass("MyClass")
        val result = useCase.getArrayType(userClassType, importsBlock)
        assertEquals("std::vector<MyClass>", result)
    }

    @Test
    fun getArrayTypeAddsVectorToImports() {
        val result = useCase.getArrayType(DataType.float64, importsBlock)
        Assertions.assertEquals(1, importsBlock.subs.size)
        assertEquals("<vector>", importsBlock.subs[0].name)
    }
}