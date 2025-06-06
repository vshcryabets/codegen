package generators.obj.input

import ce.defs.DataType
import ce.defs.NotDefined
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DataClassTests {

    @Test
    fun addStaticFieldAddsFieldWithStaticTrue() {
        val dataClass = DataClass(name = "TestClass")
        dataClass.addstaticfield("StaticField", DataType.int32, 100)
        val field = dataClass.subs.first() as DataField
        Assertions.assertEquals("StaticField", field.name)
        Assertions.assertEquals(DataType.int32, field.getType())
        Assertions.assertEquals(100, field.getValue().simple)
        Assertions.assertTrue(field.static)
    }

    @Test
    fun fieldAddsFieldWithDefaultValue() {
        val dataClass = DataClass(name = "TestClass")
        dataClass.field("Field1", DataType.string())
        val field = dataClass.subs.first() as DataField
        Assertions.assertEquals("Field1", field.name)
        Assertions.assertTrue(field.getType() is DataType.string)
        val value = field.getValue()
        Assertions.assertFalse(value.isDefined())
        Assertions.assertEquals(NotDefined, value.simple)
    }

    @Test
    fun fieldAddsFieldWithSpecifiedValue() {
        val dataClass = DataClass(name = "TestClass")
        dataClass.field("Field2", DataType.string(), "value")
        val field = dataClass.subs.first() as DataField
        Assertions.assertEquals("Field2", field.name)
        Assertions.assertTrue(field.getType() is DataType.string)
        Assertions.assertEquals("value", field.getValue().simple)
    }
}