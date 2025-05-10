package ce.treeio

import generators.obj.input.Block
import generators.obj.input.ConstantsBlock
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataField
import generators.obj.input.FieldImpl
import generators.obj.input.Input
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.Output
import generators.obj.input.OutputReusable
import generators.obj.out.OutputTree
import generators.obj.out.Region
import generators.obj.out.RegionImpl
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
        const val KEY_SOURCE_FILE = "srcFile"
        const val KEY_OUTPUT_FILE = "outFile"
        const val KEY_BASE_FOLDER = "baseFolder"
        const val KEY_TARGET = "target"
        const val KEY_STATIC = "static"
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
        val tagName = when (node) {
            is RegionImpl -> Region::class.java.simpleName
            else -> node.javaClass.simpleName
        }

        val element = doc.createElement(tagName)
        if (node.name.isNotEmpty())
            element.setAttribute(KEY_NAME, node.name)
        if (node is Block) {
            element.setAttribute(KEY_SOURCE_FILE, node.sourceFile)
            element.setAttribute(KEY_OUTPUT_FILE, node.outputFile)
            element.setAttribute(KEY_BASE_FOLDER, node.objectBaseFolder)
        }
        when (node) {
            is Input,
            is Output,
            is OutputReusable -> {
                element.setAttribute(KEY_TYPE, dataTypeSerializer.stringValue((node as FieldImpl).getType()))
            }
            is ConstantsEnum -> {
                element.setAttribute(KEY_DEFAULT_TYPE, dataTypeSerializer.stringValue(node.defaultDataType))
            }
            is ConstantsBlock -> {
                element.setAttribute(KEY_DEFAULT_TYPE, dataTypeSerializer.stringValue(node.defaultDataType))
            }
            is DataField -> {
                element.setAttribute(KEY_TYPE, dataTypeSerializer.stringValue(node.getType()))
                element.setAttribute(KEY_STATIC, node.static.toString())
            }
            is OutputTree -> element.setAttribute(KEY_TARGET, node.target.rawValue)
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