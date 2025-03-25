package ce.defs

import generators.obj.input.Block

open class DataType(
    val canBeNull: Boolean = false
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
            VOID, Unknown -> WEIGHT_NONE
            int8, int16, int32, int64 -> WEIGHT_PRIMITIVE
            uint8, uint16, uint32, uint64 -> WEIGHT_PRIMITIVE
            float32, float64, float128 -> WEIGHT_PRIMITIVE
            is string -> WEIGHT_PRIMITIVE
            bool -> WEIGHT_PRIMITIVE
            is pointer -> WEIGHT_PRIMITIVE
            is array -> WEIGHT_ARRAY
            is promise -> WEIGHT_PROMISE
            is custom -> WEIGHT_CLASS
            is userClass -> WEIGHT_CLASS
            else -> WEIGHT_CLASS
        }

    object Unknown : DataType()
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

    object int8Nullable : DataType(canBeNull = true)
    object int16Nullable : DataType(canBeNull = true)
    object int32Nullable : DataType(canBeNull = true)
    object int64Nullable : DataType(canBeNull = true)

    object uint8Nullable : DataType(canBeNull = true)
    object uint16Nullable : DataType(canBeNull = true)
    object uint32Nullable : DataType(canBeNull = true)
    object uint64Nullable : DataType(canBeNull = true)

    object float32Nullable : DataType(canBeNull = true)
    object float64Nullable : DataType(canBeNull = true)
    object float128Nullable : DataType(canBeNull = true)

    class string(canBeNull: Boolean = false) : DataType(canBeNull)
    object bool : DataType()
    object boolNullable : DataType(canBeNull = true)
    class pointer(val subType: DataType) : DataType(canBeNull = true)
    class array(val elementDataType: DataType, nullable : Boolean = false) : DataType(nullable)
    class promise(val elementDataType: DataType) : DataType()
    class custom(val block: Block, nullable : Boolean = false) : DataType(nullable)
    class userClass(val path: String, nullable : Boolean = false) : DataType(nullable)
    class userClassTest2(val node: generators.obj.input.Node, nullable : Boolean = false) : DataType(nullable)
//    class nullable(val inner: DataType) : DataType(true)
}

