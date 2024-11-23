package ce.treeio

import generators.obj.input.NamespaceImpl
import org.junit.jupiter.api.Assertions.*

class XmlTreeReaderTest {

    @org.junit.jupiter.api.Test
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
                        <ConstantDesc name="OTRUNC"/>
                    </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        assertTrue(result is NamespaceImpl)
        val rootNs =result as NamespaceImpl
        assertEquals("com", rootNs.name)
        assertEquals(1, rootNs.subs.size)
    }
}