package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.RValue
import ce.defs.TargetExt
import generators.kotlin.KotlinClassData
import generators.obj.abstractSyntaxTree.ConstantDesc
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.ConstantsEnum
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.Input
import generators.obj.abstractSyntaxTree.InputList
import generators.obj.abstractSyntaxTree.InterfaceDescription
import generators.obj.abstractSyntaxTree.Leaf
import generators.obj.abstractSyntaxTree.Method
import generators.obj.abstractSyntaxTree.Namespace
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.Node
import generators.obj.abstractSyntaxTree.OutputList
import generators.obj.abstractSyntaxTree.OutputReusable
import generators.obj.abstractSyntaxTree.TypeLeaf
import generators.obj.abstractSyntaxTree.buildNamespaceTree
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.AstTree
import generators.obj.syntaxParseTree.AstTypeLeaf
import generators.obj.syntaxParseTree.CodeStyleOutputTree
import generators.obj.syntaxParseTree.CommentLeaf
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.EnumNode
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.Indent
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NamespaceDeclaration
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.OutBlockArguments
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.Region
import generators.obj.syntaxParseTree.RegionImpl
import generators.obj.syntaxParseTree.ResultLeaf
import generators.obj.syntaxParseTree.Separator
import generators.obj.syntaxParseTree.Space
import generators.obj.syntaxParseTree.VariableName
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
    override fun getTags() = listOf(KotlinClassData::class.java.simpleName)
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

class FieldNodeDeserializer : NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = FieldNode(args.name)
    override fun getTags() = listOf(FieldNode::class.java.simpleName)
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

class ArgumentsDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = Arguments(args.name)
    override fun getTags() = listOf(Arguments::class.java.simpleName)
}

class RValueDeserializer: NodeDeserializer {
    override operator fun invoke(args: DeserializeArguments) = RValue(args.name)
    override fun getTags() = listOf(RValue::class.java.simpleName)
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
