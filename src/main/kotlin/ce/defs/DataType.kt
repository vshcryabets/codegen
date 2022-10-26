package ce.defs

open class DataType(
    val nullable: Boolean = false
) {
    companion object {
        const val WEIGHT_NONE = 0
        const val WEIGHT_PRIMITIVE = 1
        const val WEIGHT_ARRAY = 2
        const val WEIGHT_PROMISE = 3
        const val WEIGHT_CLASS = 4
    }
    fun getWeight(): Int =
        when (this) {
            VOID -> WEIGHT_NONE
            int8, int16, int32, int64 -> WEIGHT_PRIMITIVE
            uint8, uint16, uint32, uint64 -> WEIGHT_PRIMITIVE
            float32, float64, float128 -> WEIGHT_PRIMITIVE
            string -> WEIGHT_PRIMITIVE
            bool -> WEIGHT_PRIMITIVE
            is pointer -> WEIGHT_PRIMITIVE
            is array -> WEIGHT_ARRAY
            is promise -> WEIGHT_PROMISE
            is userClass -> WEIGHT_CLASS
            else -> WEIGHT_CLASS
        }

    object VOID : DataType()

    object int8 : DataType()
    object int16 : DataType()
    object int32 : DataType()
    object int64 : DataType()

    object uint8 : DataType()
    object uint16 : DataType()
    object uint32 : DataType()
    object uint64 : DataType()

    object float32 : DataType()
    object float64 : DataType()
    object float128 : DataType()

    object string : DataType()
    object bool : DataType()
    class pointer(val subType: DataType) : DataType()
    class array(val elementDataType: DataType, nullable : Boolean = false) : DataType(nullable)
    class promise(val elementDataType: DataType) : DataType()
    class userClass(val path: String, nullable : Boolean = false) : DataType(nullable)
}

