package generators.kotlin

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.RValue
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.Input
import generators.obj.abstractSyntaxTree.NewInstance
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.Constructor
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.VariableName

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
            DataType.uint8, DataType.uint16, DataType.uint32, DataType.uint64 ->
                RValue(name = value.simple.toString())

            DataType.float32 -> RValue(name = value.simple.toString() + "f")
            DataType.float64 -> RValue(name = value.simple.toString())
            DataType.bool -> RValue(name = value.simple.toString())
            is DataType.string -> {
                if (value.simple is String) {
                    RValue(name = "\"${value.simple}\"")
                } else {
                    throw IllegalArgumentException(
                        "Expected a String value for DataType.string, " +
                                "but got: ${value.simple}"
                    )
                }
            }

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
                val arguments = Arguments()
                addSub(arguments)
                item.subs
                    .filter { it is Input }
                    .forEach {
                        val input = it as Input
                        val argumentNode = ArgumentNode()
                        arguments.addSub(
                            argumentNode
                        )
                        argumentNode.addSub(VariableName(it.name))
                        argumentNode.addSub(Keyword("="))
                        argumentNode.addSub(
                            toRightValue(
                                type = input.getType(),
                                value = input.getValue(),
                                fileData = fileData
                            )
                        )
                    }
            }
        )
        return result
    }
}