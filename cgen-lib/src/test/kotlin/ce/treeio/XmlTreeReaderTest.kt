package ce.treeio

import ce.defs.DataType
import ce.defs.DataValue
import ce.defs.NotDefined
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.NamespaceImpl
import org.junit.jupiter.api.Assertions.*
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
        assertEquals(DataType.int32, constant1.type)
        assertEquals(NotDefined, constant1.value.value)
        val constant5 = constantsBlock.subs[5] as ConstantDesc
        assertEquals(100, constant5.value.value)
    }

}