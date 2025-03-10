package ce.defs

open class DataValue(
    val value : Any?
) {
    fun notDefined(): Boolean = (value == NotDefined)
    fun isDefined(): Boolean = value != NotDefined
}

open class IntValue(
    value: Long,
    val preferredRadix: Int
) : DataValue(value)

object NotDefined
object NotDefinedValue : DataValue(NotDefined)