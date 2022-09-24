package ce.defs

sealed class DataType {
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

