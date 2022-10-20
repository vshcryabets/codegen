package ce.defs

sealed class DataType {
    fun getWeight(): Int =
        when (this) {
            VOID -> 0
            int8, int16, int32, int64 -> 1
            uint8, uint16, uint32, uint64 -> 1
            float32, float64, float128 -> 1
            string -> 1
            bool -> 1
            is array -> 2
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
    class array(val elementDataType: DataType) : DataType()
}

