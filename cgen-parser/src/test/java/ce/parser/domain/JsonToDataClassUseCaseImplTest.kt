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
                    "c": "Test string",
                    "d": -12.34
                }
            """.trimIndent(),
            "Struct1"
        )
        assertEquals(1, res.size)
        val dataClass = res[0] as DataClass
        assertEquals(4, dataClass.subs.size)
    }

    @Test
    fun testStructWithArrayOfPrimitives() {
        val useCase = JsonToDataClassUseCaseImpl()
        val res = useCase(
            json = """
                {
                    "a": 10,
                    "b": true,
                    "c": [1,2,3,4,5,6]
                }
            """.trimIndent(),
            "Struct1"
        )
        assertEquals(1, res.size)
        val dataClass = res[0] as DataClass
        assertEquals(3, dataClass.subs.size)
    }

    @Test
    fun testStructWithInnerStruct() {
        val useCase = JsonToDataClassUseCaseImpl()
        val res = useCase(
            json = """
                {
                    "a": 10,
                    "b": true,
                    "cStruct": {"s1": true, "s2": 25}
                }
            """.trimIndent(),
            "Struct1"
        )
        assertEquals(1, res.size)
        val dataClass = res[0] as DataClass
        assertEquals(4, dataClass.subs.size)
        assertTrue(false)
    }

    @Test
    fun testStructWithArrayOfStructs() {
        assertTrue(false)
    }

}