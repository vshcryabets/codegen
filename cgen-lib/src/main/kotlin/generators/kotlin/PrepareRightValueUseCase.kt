package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import generators.obj.input.DataField
import generators.obj.input.NewInstance
import generators.obj.input.addSub
import generators.obj.out.FileData

class PrepareRightValueUseCase(
    private val getTypeNameUseCase: GetTypeNameUseCase
) {
    fun toRightValue(dataField: DataField, fileData: FileData): DataValue =
        toRightValue(
            type = dataField.type,
            value = dataField.value,
            fileData = fileData
        )

    fun toRightValue(type: DataType, value: DataValue, fileData: FileData): DataValue =
        when (type) {
            DataType.VOID -> DataValueImpl(name = "void")
            DataType.int8, DataType.int16, DataType.int32, DataType.int64,
            DataType.uint8, DataType.uint16, DataType.uint32, DataType.uint64 -> DataValueImpl(name = value.simple.toString())

            DataType.float32 -> DataValueImpl(name = value.simple.toString() + "f")
            DataType.float64 -> DataValueImpl(name = value.simple.toString())
            DataType.bool -> DataValueImpl(name = value.simple.toString())
            is DataType.string -> DataValueImpl(name = value.simple.toString())
            is DataType.custom -> {
                if (!value.isComplex) {
                    DataValueImpl(name = value.simple.toString())
                } else {
                    val valueComplexItem = value.subs.first()
                    if (valueComplexItem is NewInstance) {
                        prepareConstructor(valueComplexItem, fileData)
                    } else {
                        DataValueImpl(name = "QQVAL_complex???")
                    }
                }

            }

            else -> DataValueImpl(name = "QQVAL_$type")
        }

    private fun prepareConstructor(item: NewInstance, fileData: FileData): DataValue {
        val result = DataValueImpl(isComplex = true)
        result.addSub(
            NewInstance(
                name = getTypeNameUseCase.typeTo(
                    file = fileData,
                    type = item.type
                ), type = item.type
            )
        )
        return result
    }
}