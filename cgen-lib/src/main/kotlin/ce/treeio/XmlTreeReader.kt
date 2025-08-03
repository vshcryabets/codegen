package ce.treeio

import ce.defs.DataType
import generators.obj.input.Block
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.TreeRoot
import generators.obj.input.addSub
import generators.obj.input.getPath
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


class XmlTreeReaderError(message: String, inner: Exception) : Exception(message, inner) {}

class XmlTreeReader : TreeReader {
    val dataTypeSerializer = DataTypeSerializer()

    val map = mutableMapOf<String, NodeDeserializer>()

    init {
        arrayListOf(
            CommentsBlockDeserializer(),
            CommentLeafDeserializer(),
            FileDataDeserializer(),
            NamespaceDeclarationDeserializer(),
            IndentDeserializer(),
            OutBlockDeserializer(),
            OutBlockArgumentsDeserializer(),
            EnumNodeDeserializer(),
            SeparatorDeserializer(),
            NlSeparatorDeserializer(),
            FieldNodeDeserializer(),
            KeywordDeserializer(),
            AstTypeLeafDeserializer(),
            DataValueDeserializer(),
            VariableNameDeserializer(),
            ResultLeafDeserializer(),
            ArgumentNodeDeserializer(),
            ImportsBlockDeserializer(),
            RegionDeserializer(),
            SpaceDeserializer(),
            KotlinClassDataDeserializer(),
            NamespaceDeserializer(),
            ConstantsEnumDeserializer(dataTypeSerializer),
            ConstantsBlockDeserializer(dataTypeSerializer),
            DataClassDeserializer(),
            InterfaceDescriptionDeserializer(),
            TypeLeafDeserializer(dataTypeSerializer),
            MethodDeserializer(),
            OutputListDeserialize(),
            InputListDeserialize(),
            InputDeserializer(),
            OutputReusableDeserializer(),
            DataFieldDeserializer(),
            OutTreeDeserizalier(),
            AstTreeDeserializer(),
            CodeStyleOututTreeDeserializer(),
            ConstantDescDeserializer(),
            ArgumentsDeserializer(),
            RValueDeserializer()
        ).forEach {
            it.getTags().forEach { tag ->
                map[tag] = it
            }
        }
    }

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
            val result: Leaf
            if (map.containsKey(tagName)) {
                val deserializer = map[tagName]!!
                result = deserializer.invoke(
                    DeserializeArguments(
                        name = name,
                        element = node,
                        sourceFile = sourceFile,
                        parent = parent
                    )
                )
            } else {
                throw IllegalStateException("Unknown tag \"$tagName\" in XML tree. ")
            }
            result.setParent2(parent)
            if (result is Block) {
                result.sourceFile = sourceFile
                result.outputFile = node.getAttribute(XmlInTreeWritterImpl.KEY_OUTPUT_FILE)
                result.objectBaseFolder = node.getAttribute(XmlInTreeWritterImpl.KEY_BASE_FOLDER)
            }

            val childNodesCount = node.childNodes.length
            if (childNodesCount > 0) {
                if (result !is Node)
                    throw IllegalStateException("Object $tagName is not a Node")
                // Namespace can be translated to the chain of nodes
                // and we need to find the last node in the chain
                // For example:
                // <Namespace name="com.goldman.xml">
                // will become:
                // <com>
                //   |-<goldman>
                //         |-<xml/>
                val nextRoot = getLast(result)
                for (i in 0..childNodesCount - 1) {
                    val childNode = node.childNodes.item(i)
                    if (childNode != null && childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        nextRoot.addSub(xmlToTree(childNode as Element, nextRoot, defaultSourceFile))
                    }
                }
            }
            if (result is ConstantDesc) {
                val constantsBlock = result.getParent2() as ConstantsBlock
                if (result.getType() == DataType.Unknown) {
                    result.setType(constantsBlock.defaultDataType)
                }
            }
            return result
        } catch (error: XmlTreeReaderError) {
            throw error
        } catch (err: Exception) {
            throw XmlTreeReaderError("Error while parsing ${parent.getPath()}/${node.tagName}", err)
        }
    }

    private fun getLast(node: Node): Node {
        var last = node
        while (last.subs.isNotEmpty()) {
            val leaf =
                last.subs.firstOrNull { it is Node } ?: throw IllegalStateException("Can' get last node from $node")
            last = leaf as Node
        }
        return last
    }

}
