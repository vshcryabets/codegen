package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.TargetExt
import generators.kotlin.KotlinClassData
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.input.Input
import generators.obj.input.InputList
import generators.obj.input.InterfaceDescription
import generators.obj.input.Leaf
import generators.obj.input.Method
import generators.obj.input.Namespace
import generators.obj.input.NamespaceImpl
import generators.obj.input.Node
import generators.obj.input.OutputList
import generators.obj.input.OutputReusable
import generators.obj.input.TypeLeaf
import generators.obj.input.buildNamespaceTree
import generators.obj.input.setValue
import generators.obj.out.ArgumentNode
import generators.obj.out.AstTree
import generators.obj.out.AstTypeLeaf
import generators.obj.out.CodeStyleOutputTree
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.ConstantNode
import generators.obj.out.EnumNode
import generators.obj.out.FileData
import generators.obj.out.FileDataImpl
import generators.obj.out.ImportsBlock
import generators.obj.out.Indent
import generators.obj.out.Keyword
import generators.obj.out.MultilineCommentsBlock
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.NlSeparator
import generators.obj.out.OutBlock
import generators.obj.out.OutBlockArguments
import generators.obj.out.OutputTree
import generators.obj.out.Region
import generators.obj.out.RegionImpl
import generators.obj.out.ResultLeaf
import generators.obj.out.Separator
import generators.obj.out.Space
import generators.obj.out.VariableName
import org.w3c.dom.Element

data class DeserializeArguments(
    val element: Element,
    val name: String,
    val sourceFile: String,
    val parent: Node?
)

interface NodeDeserializer {
    operator fun invoke(args: DeserializeArguments): Leaf
    fun getTags(): List<String>
}

class CommentsBlockDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = CommentsBlock()
    override fun getTags() = listOf(CommentsBlock::class.java.simpleName)
}

class CommentLeafDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = CommentLeaf(args.name)
    override fun getTags() = listOf(CommentLeaf::class.java.simpleName)
}

class FileDataDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = FileDataImpl(args.name)
    override fun getTags() = listOf(FileDataImpl::class.java.simpleName, FileData::class.java.simpleName)
}

class NamespaceDeclarationDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = NamespaceDeclaration(args.name)
    override fun getTags() = listOf(NamespaceDeclaration::class.java.simpleName)
 }

class KotlinClassDataDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = KotlinClassData(args.name)
    override fun getTags() = listOf(NamespaceDeclaration::class.java.simpleName)
}

class IndentDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Indent()
    override fun getTags() = listOf(Indent::class.java.simpleName)
}

class OutBlockDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = OutBlock(args.name)
    override fun getTags() = listOf(OutBlock::class.java.simpleName)
}

class OutBlockArgumentsDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = OutBlockArguments(args.name)
    override fun getTags() = listOf(OutBlockArguments::class.java.simpleName)
}

class EnumNodeDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = EnumNode(args.name)
    override fun getTags() = listOf(EnumNode::class.java.simpleName)
}

class SeparatorDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Separator(args.name)
    override fun getTags() = listOf(Separator::class.java.simpleName)
}

class NlSeparatorDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = NlSeparator(args.name)
    override fun getTags() = listOf(NlSeparator::class.java.simpleName)
}

class ConstantNodeDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = ConstantNode(args.name)
    override fun getTags() = listOf(ConstantNode::class.java.simpleName)
}

class KeywordDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Keyword(args.name)
    override fun getTags() = listOf(Keyword::class.java.simpleName)
}

class AstTypeLeafDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = AstTypeLeaf(args.name)
    override fun getTags() = listOf(AstTypeLeaf::class.java.simpleName)
}

class DataValueDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = DataValueImpl(args.name, simple = args.name)
    override fun getTags() = listOf(DataValueImpl::class.java.simpleName, DataValue::class.java.simpleName)
}

class VariableNameDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = VariableName(args.name)
    override fun getTags() = listOf(VariableName::class.java.simpleName)
}

class ResultLeafDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = ResultLeaf(args.name)
    override fun getTags() = listOf(ResultLeaf::class.java.simpleName)
}

class ArgumentNodeDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = ArgumentNode(args.name)
    override fun getTags() = listOf(ArgumentNode::class.java.simpleName)
}

class MultilineCommentsBlockDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = MultilineCommentsBlock()
    override fun getTags() = listOf(MultilineCommentsBlock::class.java.simpleName)
}

class ImportsBlockDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = ImportsBlock()
    override fun getTags() = listOf(ImportsBlock::class.java.simpleName)
}

class RegionDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = RegionImpl()
    override fun getTags() = listOf(RegionImpl::class.java.simpleName, Region::class.java.simpleName)
}

class SpaceDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Space(args.name)
    override fun getTags() = listOf(Space::class.java.simpleName)
}

class NamespaceDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = buildNamespaceTree(args.name)

    override fun getTags() = listOf(
        Namespace::class.java.simpleName,
        NamespaceImpl::class.java.simpleName
    )
}

class ConstantsEnumDeserializer(
    private val dataTypeSerializer: DataTypeSerializer
): NodeDeserializer {
    override fun invoke(args: DeserializeArguments): Leaf {
        val dataTypeStr = args.element.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE)
        return ConstantsEnum(
            name = args.name,
            sourceFile = args.sourceFile,
            outputFile = args.element.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
            objectBaseFolder = args.element.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
            defaultDataType = if (dataTypeStr.isNotEmpty())
                dataTypeSerializer.fromStringValue(dataTypeStr)
            else
                DataType.VOID
        )
    }
    override fun getTags() = listOf(ConstantsEnum::class.java.simpleName)
}

class ConstantsBlockDeserializer(
    private val dataTypeSerializer: DataTypeSerializer
): NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = ConstantsBlock(
        name = args.name,
        sourceFile = args.sourceFile,
        outputFile = args.element.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
        objectBaseFolder = args.element.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
        defaultDataType = dataTypeSerializer.fromStringValue(args.element.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE))
        )

    override fun getTags() = listOf(ConstantsBlock::class.java.simpleName)
}

class DataClassDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = DataClass(
        name = args.name,
        sourceFile = args.sourceFile,
        outputFile = args.element.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
        objectBaseFolder = args.element.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER)
    )

    override fun getTags() = listOf(DataClass::class.java.simpleName)
}

class InterfaceDescriptionDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = InterfaceDescription(
        name = args.name,
        sourceFile = args.sourceFile,
        outputFile = args.element.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
        objectBaseFolder = args.element.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER)
    )

    override fun getTags() = listOf(InterfaceDescription::class.java.simpleName)
}

class TypeLeafDeserializer(
    private val dataTypeSerializer: DataTypeSerializer
): NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = TypeLeaf(
        type = dataTypeSerializer.fromStringValue(args.name)
    )

    override fun getTags() = listOf(TypeLeaf::class.java.simpleName)
}

class MethodDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Method(args.name)
    override fun getTags() = listOf(Method::class.java.simpleName)
}

class OutputListDeserialize: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = OutputList()
    override fun getTags() = listOf(OutputList::class.java.simpleName)
}

class InputListDeserialize: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = InputList()
    override fun getTags() = listOf(InputList::class.java.simpleName)
}

class InputDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Input(args.name)
    override fun getTags() = listOf(Input::class.java.simpleName)
}

class OutputReusableDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = OutputReusable(args.name)
    override fun getTags() = listOf(OutputReusable::class.java.simpleName)
}

class DataFieldDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = DataField(
        name = args.name,
        static = args.element.getAttribute(XmlInTreeWritterImpl.KEY_STATIC).toBoolean()
    )

    override fun getTags() = listOf(DataField::class.java.simpleName)
}

class OutTreeDeserizalier: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) =
        OutputTree(TargetExt.findByName(args.element.getAttribute(XmlInTreeWritterImpl.KEY_TARGET)))
    override fun getTags() = listOf(OutputTree::class.java.simpleName)
}

class AstTreeDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) =
        AstTree(TargetExt.findByName(args.element.getAttribute(XmlInTreeWritterImpl.KEY_TARGET)))
    override fun getTags() = listOf(AstTree::class.java.simpleName)
}

class CodeStyleOututTreeDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) =
        CodeStyleOutputTree(TargetExt.findByName(args.element.getAttribute(XmlInTreeWritterImpl.KEY_TARGET)))
    override fun getTags() = listOf(CodeStyleOutputTree::class.java.simpleName)
}

class ConstantDescDeserializer: NodeDeserializer {
    fun fromString(value: String, dataType: DataType) : DataValue {
        if (value.isEmpty()) {
            return DataValueImpl.NotDefinedValue
        }
        val result = when (dataType) {
            DataType.VOID -> DataValueImpl.NotDefinedValue
            DataType.int8 -> DataValueImpl(simple = value.toByte())
            DataType.int16 -> DataValueImpl(simple = value.toShort())
            DataType.int32 -> DataValueImpl(simple = value.toInt())
            DataType.int64 -> DataValueImpl(simple = value.toLong())
            DataType.uint8 -> DataValueImpl(simple = value.toUByte())
            DataType.uint16 -> DataValueImpl(simple = value.toUShort())
            DataType.uint32 -> DataValueImpl(simple = value.toUInt())
            DataType.uint64 -> DataValueImpl(simple = value.toULong())
            DataType.float32, DataType.float64, DataType.float128 -> DataValueImpl(simple = value.toDouble())
            is DataType.string -> DataValueImpl(simple = value)
            DataType.bool -> DataValueImpl(simple = value.toBoolean())
            else -> throw IllegalStateException("Unsupported dataValue for data type $dataType")
        }
        return result
    }

    override operator fun invoke(args: DeserializeArguments): Leaf {
        if (args.parent !is ConstantsBlock)
            throw IllegalStateException("ConstantDesc can be declared only in the ConstantsBlock")
        return ConstantDesc(args.name).apply {
            if (args.element.hasAttribute(XmlInTreeWritterImpl.KEY_VALUE)) {
                this.setValue(
                    fromString(
                        args.element.getAttribute(XmlInTreeWritterImpl.KEY_VALUE),
                        args.parent.defaultDataType
                    )
                )
            }
        }
    }
    override fun getTags() = listOf(ConstantDesc::class.java.simpleName)
}
