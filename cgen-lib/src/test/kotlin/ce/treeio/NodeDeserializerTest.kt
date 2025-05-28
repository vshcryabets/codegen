package ce.treeio

import generators.obj.out.CommentsBlock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.w3c.dom.Element


class NodeDeserializerTest {
    private fun mockElement(): Element {
        return org.mockito.Mockito.mock(Element::class.java)
    }

    @Test
    fun returnsCommentsBlockInstance() {
        val deserializer = CommentsBlockDeserializer()
        val args = DeserializeArguments(
            element = mockElement(),
            name = "TestCommentsBlock",
            sourceFile = "testFile.xml",
            parent = null
        )
        val result = deserializer.invoke(args)
        assertEquals(CommentsBlock::class.java, result::class.java)
    }

    @Test
    fun returnsCorrectTags() {
        val deserializer = CommentsBlockDeserializer()
        val tags = deserializer.getTags()
        assertEquals(listOf("CommentsBlock"), tags)
    }

}