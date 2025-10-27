package generators.cpp

import ce.defs.DataType
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.abstractSyntaxTree.getPath
import generators.obj.syntaxParseTree.ImportsBlock

class GetTypeNameUseCase(
    private val arrayDataType: GetArrayDataTypeUseCase
) {
    fun typeTo(importsBlock: ImportsBlock,
               type: DataType
    ) : String {
        when (type) {
            is DataType.custom ->
                importsBlock.addInclude("${type.block.getParentPath()}.${type.block.name}");
            else -> {}
        }
        when (type) {
            DataType.int8,
            DataType.int16,
            DataType.int32,
            DataType.int64,
            DataType.uint8,
            DataType.uint16,
            DataType.uint32,
            DataType.uint64,
            DataType.int8Nullable,
            DataType.int16Nullable,
            DataType.int32Nullable,
            DataType.int64Nullable,
            DataType.uint8Nullable,
            DataType.uint16Nullable,
            DataType.uint32Nullable,
            DataType.uint64Nullable -> {
                importsBlock.addInclude("<cstdint>")
            }
            DataType.string, DataType.stringNullable -> {
                importsBlock.addInclude("<string>")
            }
            is DataType.array -> {
                importsBlock.addInclude("<vector>")
            }
            else -> {}
        }
        if (type.canBeNull) {
            importsBlock.addInclude("<optional>")
        }
        val baseType = when (type) {
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

            DataType.string, DataType.stringNullable -> "std::string"
            is DataType.array -> arrayDataType.getArrayType(type.elementDataType, importsBlock)
            is DataType.userClass -> type.path
            is DataType.custom -> type.block.name
            is DataType.userClassTest2 -> type.node.getPath()
            else -> "ktQQTP_$type"
        }
        val result = if (type.canBeNull) "std::optional<$baseType>" else baseType
        return result
    }
}