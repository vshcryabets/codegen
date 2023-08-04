package ce.treeio

import ce.defs.Target
import generators.kotlin.KotlinClassData
import generators.obj.input.*
import generators.obj.out.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


class XmlTreeReader : TreeReader {
    val dataTypeSerializer = DataTypeSerializer()
    val dataValueSerializer = DataValueSerializer()

    override fun load(filePath: String): Leaf {
        val file = File(filePath)
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc: Document = db.parse(file)
        doc.getDocumentElement().normalize()
        val root = doc.documentElement
        return xmlToTree(root, TreeRoot)
    }

    private fun xmlToTree(node: Element, parent: Node): Leaf {
        val tagName = node.tagName
        val name = node.getAttribute(XmlInTreeWritterImpl.KEY_NAME)
        return when (tagName) {
            Namespace::class.java.simpleName -> NamespaceImpl(name, parent)
            ConstantsEnum::class.java.simpleName -> ConstantsEnum(
                name = name,
                parent = parent,
                sourceFile = node.getAttribute(XmlInTreeWritterImpl.KEY_SOURCE_FILE),
                outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                defaultDataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE))
            )

            CommentsBlock::class.java.simpleName -> CommentsBlock()
            CommentLeaf::class.java.simpleName -> CommentLeaf(name)
            "ConstantsBlock" -> ConstantsBlock(
                name = name,
                parent = parent,
                sourceFile = node.getAttribute(XmlInTreeWritterImpl.KEY_SOURCE_FILE),
                outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                defaultDataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE))
            )

            "DataClass" -> DataClass(
                name = name,
                parent = parent,
                sourceFile = node.getAttribute(XmlInTreeWritterImpl.KEY_SOURCE_FILE),
                outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                )
            "InterfaceDescription" -> InterfaceDescription(
                name = name,
                parent = parent,
                sourceFile = node.getAttribute(XmlInTreeWritterImpl.KEY_SOURCE_FILE),
                outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                )
            "Method" -> Method(name)
            "OutputList" -> OutputList()
            "InputList" -> InputList()
            "Output" -> Output(
                name,
                dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
            )

            "Input" -> {
                val dataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
                Input(
                    name, dataType,
                    dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                )
            }

            "OutputReusable" -> OutputReusable(
                name,
                dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
            )

            "DataField" -> {
                val dataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
                DataField(
                    name = name,
                    type = dataType,
                    value = dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                )
            }

            "ConstantDesc" -> {
                val dataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
                ConstantDesc(
                    name = name,
                    parent = parent,
                    type = dataType,
                    value =
                    dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                )
            }
            // OUT TREE
            ProjectOutput::class.java.simpleName -> ProjectOutput(
                Target.findByName(node.getAttribute(XmlInTreeWritterImpl.KEY_TARGET)))
            FileData::class.java.simpleName -> FileDataImpl(name, parent)
            NamespaceDeclaration::class.java.simpleName -> NamespaceDeclaration(name)
            KotlinClassData::class.java.simpleName -> KotlinClassData(name)
            BlockPreNewLines::class.java.simpleName -> BlockPreNewLines()
            OutBlock::class.java.simpleName -> OutBlock(name)
            OutBlockArguments::class.java.simpleName -> OutBlockArguments(name)
            EnumLeaf::class.java.simpleName -> EnumLeaf(name)
            Separator::class.java.simpleName -> Separator(name)
            NlSeparator::class.java.simpleName -> NlSeparator(name)
            ConstantLeaf::class.java.simpleName -> ConstantLeaf()
            Keyword::class.java.simpleName -> Keyword(name)
            Datatype::class.java.simpleName -> Datatype(name)
            RValue::class.java.simpleName -> RValue(name)
            VariableName::class.java.simpleName -> VariableName(name)
            ResultLeaf::class.java.simpleName -> ResultLeaf(name)
            ArgumentLeaf::class.java.simpleName -> ArgumentLeaf(name)
            MultilineCommentsBlock::class.java.simpleName -> MultilineCommentsBlock()

            else -> throw IllegalStateException("Unknown $tagName")
        }.also {
            if (it is Block) {
                it.sourceFile = node.getAttribute(XmlInTreeWritterImpl.KEY_SOURCE_FILE)
                it.outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE)
                it.objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER)
            }
            if (node.childNodes.length > 0) {
                for (i in 0..node.childNodes.length - 1) {
                    val subnode = node.childNodes.item(i)
                    if (subnode != null && subnode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        (it as Node).addSub(xmlToTree(subnode as Element, it))
                    }
                }

            }
        }
    }
}