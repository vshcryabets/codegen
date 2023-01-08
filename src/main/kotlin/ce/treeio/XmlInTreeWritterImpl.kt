package ce.treeio

import generators.obj.input.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


class XmlInTreeWritterImpl : TreeWritter {

    companion object {
        const val KEY_NAME = "name"
        const val KEY_DEFAULT_TYPE = "defaultType"
        const val KEY_TYPE = "type"
        const val KEY_VALUE = "value"
    }

    val dataTypeSerializer = DataTypeSerializer()
    val dataValueSerializer = DataValueSerializer()
    override fun storeTree(filePath: String, tree: Leaf) {
        val factory = DocumentBuilderFactory.newInstance()
        val doc: Document = factory.newDocumentBuilder().newDocument()

        val rootElement: Element =  treeToXml(doc, tree)
        doc.appendChild(rootElement)

        val tf: Transformer = TransformerFactory.newInstance().newTransformer()
        tf.setOutputProperty(OutputKeys.INDENT, "yes")
        tf.setOutputProperty(OutputKeys.METHOD, "xml")
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")

        val domSource = DOMSource(doc)
        val sr = StreamResult(File(filePath))
        tf.transform(domSource, sr)
    }

    private fun treeToXml(doc: Document, node: Leaf): Element {
        val tagName = node.javaClass.simpleName
        val element = doc.createElement(tagName)
        element.setAttribute(KEY_NAME, node.name)
        when (node) {
            is ConstantsEnum -> {
                element.setAttribute(KEY_DEFAULT_TYPE, dataTypeSerializer.stringValue(node.defaultDataType))
            }
            is ConstantsBlock -> {
                element.setAttribute(KEY_DEFAULT_TYPE, dataTypeSerializer.stringValue(node.defaultDataType))
            }
            is DataField -> {
                element.setAttribute(KEY_TYPE, dataTypeSerializer.stringValue(node.type))
                dataValueSerializer.stringValue(node.value)?.also {
                    element.setAttribute(KEY_VALUE, it)
                }
            }
            else -> {}
        }
        if (node is Node) {
            node.subs.forEach {
                element.appendChild(treeToXml(doc, it))
            }
        }
        return element
    }
}