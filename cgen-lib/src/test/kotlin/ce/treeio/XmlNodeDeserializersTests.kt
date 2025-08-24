package ce.treeio

import ce.defs.DataType
import generators.obj.abstractSyntaxTree.ConstantDesc
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.DataField
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class XmlNodeDeserializersTests {

    fun getElement(xmlString: String): Element {
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc: Document = db.parse(InputSource(StringReader(xmlString)))
        doc.documentElement.normalize()
        return doc.documentElement
    }

    @Test
    fun constantDescWithValue() {
        val deserializer = ConstantDescDeserializer()
        val parent = ConstantsBlock(name = "ValidBlock", sourceFile = "source.xml")
        parent.defaultDataType = DataType.int32
        val result = deserializer.invoke(
                DeserializeArguments(
                    element = getElement("<ConstantDesc name=\"OTRUNC\" value=\"100\"/>"),
                    name = "OTRUNC",
                    sourceFile = "source.xml",
                    parent = parent
                )
            )
        Assertions.assertTrue(result is ConstantDesc)
        Assertions.assertEquals("OTRUNC", (result as ConstantDesc).name)
        Assertions.assertEquals(100, result.getValue().simple)
    }

    @Test
    fun constantDescThrowsExceptionForInvalidParent() {
        val deserializer = ConstantDescDeserializer()
        val exception = assertThrows<IllegalStateException> {
            deserializer.invoke(
                DeserializeArguments(
                    element = getElement("<ConstantDesc name=\"INVALID_CONSTANT\"/>"),
                    name = "INVALID_CONSTANT",
                    sourceFile = "source.xml",
                    parent = null
                )
            )
        }
        Assertions.assertEquals("ConstantDesc can be declared only in the ConstantsBlock", exception.message)
    }

    @Test
    fun constantDescParsesSuccessfullyWithValidParent() {
        val deserializer = ConstantDescDeserializer()
        val parent = ConstantsBlock(name = "ValidBlock", sourceFile = "source.xml")
        val result = deserializer.invoke(
            DeserializeArguments(
                element = getElement("<ConstantDesc name=\"VALID_CONSTANT\"/>"),
                name = "VALID_CONSTANT",
                sourceFile = "source.xml",
                parent = parent
            )
        )
        Assertions.assertTrue(result is ConstantDesc)
        Assertions.assertEquals("VALID_CONSTANT", (result as ConstantDesc).name)
    }

    @Test
    fun dataFieldParsesStaticAttributeAsTrue() {
        val deserializer = DataFieldDeserializer()
        val result = deserializer.invoke(
            DeserializeArguments(
                element = getElement("<DataField name=\"StaticField\" static=\"true\"/>"),
                name = "StaticField",
                sourceFile = "source.xml",
                parent = null
            )
        )
        Assertions.assertTrue(result is DataField)
        Assertions.assertTrue(result.static)
    }

    @Test
    fun dataFieldParsesStaticAttributeAsFalse() {
        val deserializer = DataFieldDeserializer()
        val result = deserializer.invoke(
            DeserializeArguments(
                element = getElement("<DataField name=\"NonStaticField\" static=\"false\"/>"),
                name = "NonStaticField",
                sourceFile = "source.xml",
                parent = null
            )
        )
        Assertions.assertTrue(result is DataField)
        Assertions.assertFalse(result.static)
    }

    @Test
    fun dataFieldHandlesMissingStaticAttribute() {
        val deserializer = DataFieldDeserializer()
        val result = deserializer.invoke(
            DeserializeArguments(
                element = getElement("<DataField name=\"DefaultStaticField\"/>"),
                name = "DefaultStaticField",
                sourceFile = "source.xml",
                parent = null
            )
        )
        Assertions.assertTrue(result is DataField)
        Assertions.assertFalse(result.static)
    }
}