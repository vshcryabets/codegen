package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValueImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class NewInstanceTest {

    @Test
    fun argumentAddsSubWithNode() {
        val dc1 = DataClass("argument")
        val dc1instance = NewInstance("newInstance").setType(type = DataType.custom(dc1))
        val instance = NewInstance(name = "test").setType(type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.custom(dc1), dc1instance)
        val sub = instance.subs.firstOrNull { it is DataField } as DataField?
        assertEquals("arg1", sub!!.name)
        assertEquals(dc1instance, sub.getValue().subs.first())
    }

    @Test
    fun argumentAddsSubWithDataValue() {
        val dc1 = DataClass("argument")
        val dc1instance = NewInstance("newInstance").setType(type = DataType.custom(dc1))
        val dc1dataValue = DataValueImpl(isComplex = true, simple = null)
        dc1dataValue.addSub(dc1instance)
        val instance = NewInstance(name = "test").setType(type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.custom(dc1), dc1dataValue)
        val sub = instance.subs.firstOrNull { it is DataField } as DataField?
        assertEquals("arg1", sub!!.name)
        assertEquals(dc1instance, sub.getValue().subs.first())
    }

    @Test
    fun argumentAddsSubWithCorrectValues() {
        val instance = NewInstance(name = "test").setType(type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.string(false), "value1")
        val sub = instance.subs.firstOrNull { it is DataField } as DataField?
        assertEquals("arg1", sub!!.name)
        assertEquals("value1", sub.getValue().simple)
    }

    @Test
    fun argumentAddsSubWithNullValue() {
        val instance = NewInstance(name = "test").setType(type = DataType.custom(DataClass("test")))
        instance.argument("arg2", DataType.string(), "STR")
        val sub = instance.subs.firstOrNull { it is DataField } as DataField?
        assertNotNull(sub)
        assertEquals("arg2", sub!!.name)
        assertEquals("STR", sub.getValue().simple)
    }

    @Test
    fun argumentAddsMultipleSubs() {
        val instance = NewInstance(name = "test").setType(type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.string(false), "value1")
        instance.argument("arg2", DataType.int32, 42)
        // NewInstance should have 3 subs: "TypeLeaf", "arg1", "arg2"
        assertEquals(3, instance.subs.size)
        val sub1 = instance.subs[1] as DataField
        val sub2 = instance.subs[2] as DataField
        assertEquals("arg1", sub1.name)
        assertEquals("value1", sub1.getValue().simple)
        assertEquals("arg2", sub2.name)
        assertEquals(DataType.int32, sub2.getType())
        assertEquals(42, sub2.getValue().simple)
    }
}