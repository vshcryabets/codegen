package ce.treeio

import ce.defs.DataType
import ce.defs.NotDefined
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.DataField
import generators.obj.input.NamespaceImpl
import generators.obj.input.getValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class XmlTreeReaderTest {

    @Test
    fun loadFromString() {
        val reader = XmlTreeReader()
        val result = reader.loadFromString("""
            <Namespace name="com.goldman.xml">
                    <ConstantsBlock defaultType="int32" name="ModeTypeXml">
                        <CommentsBlock>
                            <CommentLeaf name="File mode types"/>
                        </CommentsBlock>
                        <ConstantDesc name="OREAD"/>
                        <ConstantDesc name="OWRITE"/>
                        <ConstantDesc name="ORDWR"/>
                        <ConstantDesc name="OEXEC"/>
                        <ConstantDesc name="OTRUNC" value="100"/>
                    </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        assertTrue(result is NamespaceImpl)
        val rootNs =result as NamespaceImpl
        assertEquals("com", rootNs.name)
        assertEquals(1, rootNs.subs.size)

        val lastNs = rootNs.getNamespace("goldman.xml")
        val constantsBlock = lastNs.subs.first() as ConstantsBlock
        assertEquals(6, constantsBlock.subs.size)
        val constant1 = constantsBlock.subs[1] as ConstantDesc
        assertEquals(DataType.int32, constant1.getType())
        assertEquals(NotDefined, constant1.getValue().simple)
        assertEquals(1, constant1.subs.size)
        val constant5 = constantsBlock.subs[5] as ConstantDesc
        assertEquals(100, constant5.getValue().simple)
        assertEquals(2, constant5.subs.size)
    }

    @Test
    fun loadStaticField() {
        val reader = XmlTreeReader()
        val result = reader.loadFromString("""
            <DataField name="A" value="100">
                <TypeLeaf name="int32"/>
            </DataField>
        """.trimIndent())
        assertTrue(result is DataField)
        val dataField1 = result as DataField
        assertFalse(dataField1.static)

        val result2 = reader.loadFromString("""
            <DataField name="B" value="100" static="true">
                <TypeLeaf name="int32"/>
            </DataField>
        """.trimIndent())
        assertTrue(result2 is DataField)
        val dataField2 = result2 as DataField
        assertTrue(dataField2.static)
    }
}