package generators.cpp

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
            DataType.int8, DataType.int8Nullable -> "int8_t"
            DataType.int16, DataType.int16Nullable -> "int16_t"
            DataType.int32, DataType.int32Nullable -> "int32_t"
            DataType.int64, DataType.int64Nullable -> "int64_t"
            DataType.uint8, DataType.uint8Nullable -> "uint8_t"
            DataType.uint16, DataType.uint16Nullable -> "uint16_t"
            DataType.uint32, DataType.uint32Nullable -> "uint32_t"
            DataType.uint64, DataType.uint64Nullable -> "uint64_t"
            DataType.float32, DataType.float32Nullable -> "float"
            DataType.float64, DataType.float64Nullable -> "double"
            DataType.bool, DataType.boolNullable -> "bool"
            is DataType.string -> "std::string"
            is DataType.array -> arrayDataType.getArrayType(type.elementDataType)
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name
            is DataType.userClassTest2 -> type.node.getPath()
            else -> "ktQQTP_$type"
        } + (if (type.canBeNull) "?" else "")
    }
}