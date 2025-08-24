package generators.kotlin

import ce.defs.DataType
import generators.obj.abstractSyntaxTree.findOrCreateSub
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.abstractSyntaxTree.getPath
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.ImportsBlock

class GetTypeNameUseCase(
    private val arrayDataType: GetArrayDataTypeUseCase
) {
    fun typeTo(file: FileData,
               type: DataType
    ) : String {
        when (type) {
            is DataType.custom ->
                file.findOrCreateSub(ImportsBlock::class.java)
                    .addInclude("${type.block.getParentPath()}.${type.block.name}");
            else -> {}
        }
        return when (type) {
            DataType.VOID -> "void"
            DataType.int8, DataType.int8Nullable -> "Byte"
            DataType.int16, DataType.int16Nullable -> "Short"
            DataType.int32, DataType.int32Nullable -> "Int"
            DataType.int64, DataType.int64Nullable -> "Long"
            DataType.uint8, DataType.uint8Nullable -> "UByte"
            DataType.uint16, DataType.uint16Nullable -> "UShort"
            DataType.uint32, DataType.uint32Nullable -> "UInt"
            DataType.uint64, DataType.uint64Nullable -> "ULong"
            DataType.float32, DataType.float32Nullable -> "Float"
            DataType.float64, DataType.float64Nullable -> "Double"
            DataType.bool, DataType.boolNullable -> "Boolean"
            is DataType.string -> "String"
            is DataType.array -> arrayDataType.getArrayType(type.elementDataType)
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name
            is DataType.userClassTest2 -> type.node.getPath()
            else -> "ktQQTP_$type"
        } + (if (type.canBeNull) "?" else "")
    }
}