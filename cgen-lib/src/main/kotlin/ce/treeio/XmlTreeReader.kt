package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.DataValueImpl
import ce.defs.TargetExt
import generators.kotlin.KotlinClassData
import generators.obj.input.Block
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
import generators.obj.input.Output
import generators.obj.input.OutputList
import generators.obj.input.OutputReusable
import generators.obj.input.TreeRoot
import generators.obj.input.addSub
import generators.obj.input.buildNamespaceTree
import generators.obj.input.getPath
import generators.obj.out.ArgumentNode
import generators.obj.out.AstTree
import generators.obj.out.CodeStyleOutputTree
import generators.obj.out.CommentLeaf
import generators.obj.out.CommentsBlock
import generators.obj.out.ConstantNode
import generators.obj.out.Datatype
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
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


class XmlTreeReaderError(message: String, inner: Exception) : Exception(message, inner) {}

class XmlTreeReader : TreeReader {
    val dataTypeSerializer = DataTypeSerializer()
    val dataValueSerializer = DataValueSerializer()

    override fun load(filePath: String): Leaf {
        val file = File(filePath)
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc: Document = db.parse(file)
        doc.documentElement.normalize()
        val root = doc.documentElement
        return xmlToTree(root, TreeRoot, file.absolutePath)
    }

    override fun loadFromString(data: String): Leaf {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc: Document = db.parse(InputSource(StringReader(data)))
        doc.documentElement.normalize()
        val root = doc.documentElement
        return xmlToTree(root, TreeRoot, "")
    }

    private fun xmlToTree(node: Element, parent: Node, defaultSourceFile: String): Leaf {
        val tagName = node.tagName
        val name = node.getAttribute(XmlInTreeWritterImpl.KEY_NAME)
        var sourceFile = node.getAttribute(XmlInTreeWritterImpl.KEY_SOURCE_FILE)
        if (sourceFile.isEmpty())
            sourceFile = defaultSourceFile
        try {
            return when (tagName) {
                Namespace::class.java.simpleName,
                NamespaceImpl::class.java.simpleName -> buildNamespaceTree(name)
                ConstantsEnum::class.java.simpleName -> {
                    val dataTypeStr = node.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE)
                    ConstantsEnum(
                        name = name,
                        sourceFile = sourceFile,
                        outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                        objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                        defaultDataType = if (dataTypeStr.isNotEmpty())
                            dataTypeSerializer.fromStringValue(dataTypeStr)
                        else
                            DataType.VOID
                    )
                }

                CommentsBlock::class.java.simpleName -> CommentsBlock()
                CommentLeaf::class.java.simpleName -> CommentLeaf(name)
                "ConstantsBlock" -> ConstantsBlock(
                    name = name,
                    sourceFile = sourceFile,
                    outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                    objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                    defaultDataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE))
                )

                DataClass::class.java.simpleName -> DataClass(
                    name = name,
                    sourceFile = sourceFile,
                    outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE),
                    objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER),
                )

                "InterfaceDescription" -> InterfaceDescription(
                    name = name,
                    sourceFile = sourceFile,
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

                DataField::class.java.simpleName -> {
                    val dataTypeStr = node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE)
                    val dataType = if (dataTypeStr.isNotEmpty())
                        dataTypeSerializer.fromStringValue(dataTypeStr)
                    else
                        DataType.VOID
                    DataField(
                        name = name,
                        type = dataType,
                        value = dataValueSerializer.fromString(
                            node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE),
                            dataType
                        ),
                        static = node.getAttribute(XmlInTreeWritterImpl.KEY_STATIC).toBoolean()
                    )
                }

                ConstantDesc::class.java.simpleName -> {
                    if (parent !is ConstantsBlock)
                        throw IllegalStateException("ConstantDesc can be declared only in the ConstantsBlock")
                    val dataTypeString = node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE)
                    val dataType = if (dataTypeString.isEmpty())
                        parent.defaultDataType
                    else
                        dataTypeSerializer.fromStringValue(dataTypeString)
                    ConstantDesc(
                        name = name,
                        type = dataType,
                        value =
                        dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                    )
                }
                // OUT TREE
                OutputTree::class.java.simpleName -> OutputTree(
                    TargetExt.findByName(node.getAttribute(XmlInTreeWritterImpl.KEY_TARGET))
                )

                CodeStyleOutputTree::class.java.simpleName -> CodeStyleOutputTree(
                    TargetExt.findByName(node.getAttribute(XmlInTreeWritterImpl.KEY_TARGET))
                )

                AstTree::class.java.simpleName -> AstTree(
                    TargetExt.findByName(node.getAttribute(XmlInTreeWritterImpl.KEY_TARGET))
                )

                FileData::class.java.simpleName, FileDataImpl::class.java.simpleName -> FileDataImpl(name)
                NamespaceDeclaration::class.java.simpleName -> NamespaceDeclaration(name)
                KotlinClassData::class.java.simpleName -> KotlinClassData(name)
                Indent::class.java.simpleName -> Indent()
                OutBlock::class.java.simpleName -> OutBlock(name)
                OutBlockArguments::class.java.simpleName -> OutBlockArguments(name)
                EnumNode::class.java.simpleName -> EnumNode(name)
                Separator::class.java.simpleName -> Separator(name)
                NlSeparator::class.java.simpleName -> NlSeparator(name)
                ConstantNode::class.java.simpleName -> ConstantNode()
                Keyword::class.java.simpleName -> Keyword(name)
                Datatype::class.java.simpleName -> Datatype(name)
                DataValue::class.java.simpleName -> DataValueImpl(name, simple = name)
                VariableName::class.java.simpleName -> VariableName(name)
                ResultLeaf::class.java.simpleName -> ResultLeaf(name)
                ArgumentNode::class.java.simpleName -> ArgumentNode(name)
                MultilineCommentsBlock::class.java.simpleName -> MultilineCommentsBlock()
                ImportsBlock::class.java.simpleName -> ImportsBlock()
                RegionImpl::class.java.simpleName,Region::class.java.simpleName -> RegionImpl()
                Space::class.java.simpleName -> Space(name)

                else -> throw IllegalStateException("Unknown $tagName")
            }.also {
                it.setParent2(parent)
                if (it is Block) {
                    it.sourceFile = sourceFile
                    it.outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE)
                    it.objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER)
                }
                if (node.childNodes.length > 0) {
                    val nextRoot = getLast(it as Node)
                    for (i in 0..node.childNodes.length - 1) {
                        val subnode = node.childNodes.item(i)
                        if (subnode != null && subnode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            nextRoot.addSub(xmlToTree(subnode as Element, nextRoot, defaultSourceFile))
                        }
                    }

                }
            }
        } catch (error: XmlTreeReaderError) {
            throw error
        } catch (err: Exception) {
            throw XmlTreeReaderError("Error while parsing ${parent.getPath()}/${node.tagName}", err)
        }
    }

    private fun getLast(node: Node): Node {
        var last = node
        while (last.subs.isNotEmpty()) {
            val leaf = last.subs.firstOrNull { it is Node } ?: throw IllegalStateException("Can' get last node from $node")
            last = leaf as Node
        }
        return last
    }

}
