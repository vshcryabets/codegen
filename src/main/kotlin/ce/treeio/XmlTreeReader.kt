package ce.treeio

import generators.obj.input.*
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
            "Namespace" -> Namespace(name, parent)
            "ConstantsEnum" -> ConstantsEnum(name, parent,
                dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE)))
            "ConstantsBlock" -> ConstantsBlock(name, parent,
                dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_DEFAULT_TYPE)))
            "DataClass" -> DataClass(name, parent)
            "InterfaceDescription" -> InterfaceDescription(name, parent)
            "Method" -> Method(name)
            "OutputList" -> OutputList()
            "InputList" -> InputList()
            "Output" -> Output(name,
                dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE)))
            "Input" -> {
                val dataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
                Input(
                    name, dataType,
                    dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                )
            }
            "OutputReusable" -> OutputReusable(name,
                dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE)))
            "DataField" -> {
                val dataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
                DataField(
                    name, parent, dataType,
                    dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                )
            }
            "ConstantDesc" -> {
                val dataType = dataTypeSerializer.fromStringValue(node.getAttribute(XmlInTreeWritterImpl.KEY_TYPE))
                ConstantDesc(
                    name, parent, dataType,
                    dataValueSerializer.fromString(node.getAttribute(XmlInTreeWritterImpl.KEY_VALUE), dataType)
                )
            }
            else -> throw IllegalStateException("Unknown $tagName")
        }.also {
            if (node.childNodes.length > 0) {
                for (i in 0..node.childNodes.length - 1) {
                    val subnode = node.childNodes.item(i)
                    if ( subnode != null && subnode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        (it as Node).addSub(xmlToTree(subnode as Element, it))
                    }
                }

            }
        }
    }
}