package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.RValue
import generators.obj.input.DataField
import generators.obj.input.NewInstance
import generators.obj.input.addSub
import generators.obj.out.Arguments
import generators.obj.out.Constructor
import generators.obj.out.FileData

class PrepareRightValueUseCase(
    private val getTypeNameUseCase: GetTypeNameUseCase
) {
    fun toRightValue(dataField: DataField, fileData: FileData): RValue =
        toRightValue(
            type = dataField.getType(),
            value = dataField.getValue(),
            fileData = fileData
        )

    fun toRightValue(type: DataType, value: DataValue, fileData: FileData): RValue =
        when (type) {
            DataType.VOID -> RValue(name = "void")
            DataType.int8, DataType.int16, DataType.int32, DataType.int64,
            DataType.uint8, DataType.uint16, DataType.uint32, DataType.uint64 -> RValue(name = value.simple.toString())

            DataType.float32 -> RValue(name = value.simple.toString() + "f")
            DataType.float64 -> RValue(name = value.simple.toString())
            DataType.bool -> RValue(name = value.simple.toString())
            is DataType.string -> RValue(name = value.simple.toString())
            is DataType.custom -> {
                if (!value.isComplex) {
                    RValue(name = value.simple.toString())
                } else {
                    val valueComplexItem = value.subs.first()
                    if (valueComplexItem is NewInstance) {
                        prepareConstructor(valueComplexItem, fileData)
                    } else {
                        RValue(name = "QQVAL_complex???")
                    }
                }

            }

            else -> RValue(name = "QQVAL_$type")
        }

    fun prepareConstructor(item: NewInstance, fileData: FileData): RValue {
        val result = RValue()
        result.addSub(
            Constructor(
                name = getTypeNameUseCase.typeTo(
                    file = fileData,
                    type = item.getType()
                )
            ).apply {
                addSub(Arguments())
            }
        )
        return result
    }
}