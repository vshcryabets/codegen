package generators.obj

import ce.defs.DataValue
import ce.defs.DataValueImpl
import generators.obj.input.Field

class AutoincrementField {
    var previous: DataValue? = null

    operator fun invoke(field : Field) {
        if ((!field.value.isDefined()) && previous != null) {

            val previousValue = previous!!.simple
            val newValue = when (previousValue) {
                is Byte -> previousValue.toByte() + 1
                is Short -> previousValue.toShort() + 1
                is Int -> previousValue.toInt() + 1
                is Long -> previousValue.toLong() + 1
                else -> throw IllegalStateException("AutoincrementField not supporting ${previousValue!!::class.java.simpleName}")
            }

            field.value = DataValueImpl(simple = newValue)
        }

        previous = field.value
    }
}