package ce.parser.domain

import generators.obj.input.DataClass
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JsonToDataClassUseCaseImplTest {
    @Test
    fun testSimpleData() {
        val useCase = JsonToDataClassUseCaseImpl()
        val res = useCase(
            json = """
                {
                    "a": 10,
                    "b": true,
                    "c": "Test string"
                }
            """.trimIndent(),
            "Struct1"
        )
        assertEquals(1, res.size)
        val dataClass = res[0] as DataClass
        assertEquals(3, dataClass.subs.size)
    }

    @Test
    fun testStructWithArrayOfPrimitives() {
        assertTrue(false)
    }

    @Test
    fun testStructWithInnerStruct() {
        assertTrue(false)
    }

    @Test
    fun testStructWithArrayOfStructs() {
        assertTrue(false)
    }

}