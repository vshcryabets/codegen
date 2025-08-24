package ce.treeio

import ce.defs.DataType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DataTypeSerializerTest {

    @Test
    fun getBasicToString() {
        val serializer = DataTypeSerializer()
        Assertions.assertEquals("int8", serializer.stringValue(DataType.int8))
        Assertions.assertEquals(serializer.stringValue(DataType.VOID),
            "void")
        Assertions.assertEquals(serializer.stringValue(DataType.bool),
            "bool")
        Assertions.assertEquals(serializer.stringValue(DataType.float128),
            "float128")
        Assertions.assertEquals(serializer.stringValue(DataType.float32),
            "float32")
        Assertions.assertEquals(serializer.stringValue(DataType.float64),
            "float64")
        Assertions.assertEquals(serializer.stringValue(DataType.int16),
            "int16")
        Assertions.assertEquals(serializer.stringValue(DataType.int32),
            "int32")
        Assertions.assertEquals(serializer.stringValue(DataType.int64),
            "int64")
        Assertions.assertEquals(serializer.stringValue(DataType.uint16),
            "uint16")
        Assertions.assertEquals(serializer.stringValue(DataType.uint32),
            "uint32")
        Assertions.assertEquals(serializer.stringValue(DataType.uint64),
            "uint64")
        Assertions.assertEquals(serializer.stringValue(DataType.uint8),
            "uint8")
    }

    @Test
    fun getStringDataType() {
        val serializer = DataTypeSerializer()
        Assertions.assertEquals("string",
            serializer.stringValue(DataType.string))
        Assertions.assertEquals("stringNullable",
            serializer.stringValue(DataType.stringNullable))

        val stringNotNull = serializer.fromStringValue("string")
        val stringNullable = serializer.fromStringValue("stringNullable")

        Assertions.assertTrue(stringNotNull is DataType.string)
        Assertions.assertFalse(stringNotNull.canBeNull)

        Assertions.assertTrue(stringNullable is DataType.stringNullable)
        Assertions.assertTrue(stringNullable.canBeNull)
    }
}