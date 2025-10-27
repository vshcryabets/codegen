package generators.cpp

import ce.defs.DataType
import generators.obj.syntaxParseTree.ImportsBlock

class GetArrayDataTypeUseCase {
    fun getArrayType(type: DataType, importsBlock: ImportsBlock): String {
        importsBlock.addInclude("<vector>")
        return when (type) {
            DataType.int8 -> "std::vector<int8_t>"
            DataType.uint8 -> "std::vector<uint8_t>"
            DataType.int16 -> "std::vector<int16_t>"
            DataType.int32 -> "std::vector<int32_t>"
            DataType.int64 -> "std::vector<int64_t>"
            DataType.uint16 -> "std::vector<uint16_t>"
            DataType.uint32 -> "std::vector<uint32_t>"
            DataType.uint64 -> "std::vector<uint64_t>"
            DataType.float32 -> "std::vector<float>"
            DataType.float64 -> "std::vector<double>"
            DataType.string -> "std::vector<std::string>"
            is DataType.userClass -> "std::vector<${type.path}>"
            is DataType.custom -> "std::vector<${type.block.name}>"
            else -> "ktQQTP_array_$type"
        }
    }
}