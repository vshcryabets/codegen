package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValueImpl
import ce.defs.IntValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ConstantsBlockTests {
    @Test
    fun addConstantWithDefaultTypeAndValue() {
        val block = ConstantsBlock(name = "TestBlock")
        val constant = block.add("CONST1")
        Assertions.assertEquals("CONST1", constant.name)
        Assertions.assertEquals(DataType.VOID, constant.getType())
        Assertions.assertEquals(DataValueImpl.NotDefinedValue, constant.getValue())
    }

    @Test
    fun addConstantWithSpecifiedTypeAndValue() {
        val block = ConstantsBlock(name = "TestBlock")
        val constant = block.add("CONST2", DataType.int32, 42)
        Assertions.assertEquals("CONST2", constant.name)
        Assertions.assertEquals(DataType.int32, constant.getType())
        Assertions.assertEquals(42, (constant.getValue() as IntValue).simple)
    }

    @Test
    fun addConstantWithIntegerTypeAndPreferredRadix() {
        val block = ConstantsBlock(name = "TestBlock", preferredRadix = 16)
        val constant = block.add("CONST3", DataType.int32, 0x2A)
        Assertions.assertEquals("CONST3", constant.name)
        Assertions.assertEquals(DataType.int32, constant.getType())
        Assertions.assertEquals(42, (constant.getValue() as IntValue).simple)
        Assertions.assertEquals(16, (constant.getValue() as IntValue).preferredRadix)
    }

    @Test
    fun addConstantWithNullValue() {
        val block = ConstantsBlock(name = "TestBlock")
        val constant = block.add("CONST4", DataType.string(), null)
        Assertions.assertEquals("CONST4", constant.name)
        Assertions.assertTrue(constant.getType() is DataType.string)
        Assertions.assertNull(constant.getValue().simple)
    }

    @Test
    fun updatePreferredRadixChangesRadix() {
        val block = ConstantsBlock(name = "TestBlock", preferredRadix = 10)
        block.preferredRadix(8)
        Assertions.assertEquals(8, block.preferredRadix)
    }

    @Test
    fun updateDefaultTypeChangesType() {
        val block = ConstantsBlock(name = "TestBlock", defaultDataType = DataType.VOID)
        block.defaultType(DataType.string())
        Assertions.assertTrue(block.defaultDataType is DataType.string)
    }
}