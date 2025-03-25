package generators.obj.input

import ce.defs.DataType
import ce.defs.DataValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class NewInstanceTest {

    @Test
    fun argumentAddsSubWithNode() {
        val dc1 = DataClass("argument")
        val dc1instance = NewInstance("newInstance", type = DataType.custom(dc1))
        val instance = NewInstance(name = "test", type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.custom(dc1), dc1instance)
        val sub = instance.subs.first() as DataField
        assertEquals("arg1", sub.name)
        assertEquals(dc1instance, sub.value.subs.first())
    }

    @Test
    fun argumentAddsSubWithDataValue() {
        val dc1 = DataClass("argument")
        val dc1instance = NewInstance("newInstance", type = DataType.custom(dc1))
        val dc1dataValue = DataValue(isComplex = true, simple = null)
        dc1dataValue.addSub(dc1instance)
        val instance = NewInstance(name = "test", type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.custom(dc1), dc1dataValue)
        val sub = instance.subs.first() as DataField
        assertEquals("arg1", sub.name)
        assertEquals(dc1instance, sub.value.subs.first())
    }

    @Test
    fun argumentAddsSubWithCorrectValues() {
        val instance = NewInstance(name = "test", type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.string(false), "value1")
        val sub = instance.subs.first() as DataField
        assertEquals("arg1", sub.name)
        assertEquals("value1", sub.value.simple)
    }

    @Test
    fun argumentAddsSubWithNullValue() {
        val instance = NewInstance(name = "test", type = DataType.custom(DataClass("test")))
        instance.argument("arg2", DataType.string(false), null)
        val sub = instance.subs.first() as DataField
        assertEquals("arg2", sub.name)
        assertNull(sub.value.simple)
    }

    @Test
    fun argumentAddsMultipleSubs() {
        val instance = NewInstance(name = "test", type = DataType.custom(DataClass("test")))
        instance.argument("arg1", DataType.string(false), "value1")
        instance.argument("arg2", DataType.int32, 42)
        assertEquals(2, instance.subs.size)
        val sub1 = instance.subs[0] as DataField
        val sub2 = instance.subs[1] as DataField
        assertEquals("arg1", sub1.name)
        assertEquals("value1", sub1.value.simple)
        assertEquals("arg2", sub2.name)
        assertEquals(DataType.int32, sub2.type)
        assertEquals(42, sub2.value.simple)
    }
}