package generators.kotlin

import ce.defs.DataType
import generators.obj.syntaxParseTree.FileDataImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetTypeNameUseCaseTest {
    val getArrayDataTypeUseCase = GetArrayDataTypeUseCase()
    val useCase = GetTypeNameUseCase(getArrayDataTypeUseCase)

    @Test
    fun testStringNullable() {
        val fileData = FileDataImpl(
            name = "testFile",
            subs = mutableListOf(),
            isDirty = true
        )
        val result = useCase.typeTo(fileData, DataType.stringNullable)
        Assertions.assertEquals("String?", result)
    }
}