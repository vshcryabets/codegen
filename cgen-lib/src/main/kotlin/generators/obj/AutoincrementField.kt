package generators.obj

import ce.defs.DataValue
import generators.obj.input.DataField

class AutoincrementField {
    var previous: DataValue? = null

    operator fun invoke(field : DataField) {
        if ((field.value == null || field.value.notDefined()) && previous != null) {

            val previousValue = previous!!.value
            val newValue = when (previousValue) {
                is Byte -> previousValue.toByte() + 1
                is Short -> previousValue.toShort() + 1
                is Int -> previousValue.toInt() + 1
                is Long -> previousValue.toLong() + 1
                else -> throw IllegalStateException("AutoincrementField not supporting ${previousValue!!::class.java.simpleName}")
            }

            field.value = DataValue(newValue)
        }

        if (field.value != null) {
            previous = field.value
        }
    }
}