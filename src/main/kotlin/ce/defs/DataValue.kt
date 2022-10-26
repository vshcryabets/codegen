package ce.defs

open class DataValue(val value : Any?) {
    fun notDefined(): Boolean = (value == NotDefined)
}

object NotDefined
object NotDefinedValue : DataValue(NotDefined)