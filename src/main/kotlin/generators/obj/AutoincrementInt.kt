package generators.obj

import ce.defs.DataValue
import generators.obj.input.DataField

class AutoincrementInt {
    var previous: DataValue? = null

    operator fun invoke(field : DataField) {
        if ((field.value == null || field.value.notDefined()) && previous != null) {
            field.value = DataValue(previous!!.value as Int + 1)
        }

        if (field.value != null) {
            previous = field.value
        }
    }
}