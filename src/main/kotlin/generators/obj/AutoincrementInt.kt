package generators.obj

import generators.obj.input.DataField
import generators.obj.input.NotDefined

class AutoincrementInt {
    var previous: Any? = null

    operator fun invoke(field : DataField) {
        if ((field.value == null || field.value == NotDefined) && previous != null) {
            field.value = previous!! as Int + 1
        }

        if (field.value != null) {
            previous = field.value
        }
    }
}