package ce.parser.domain

import ce.parser.nnparser.Type
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NamesDictionaryRepoTest {
    @Test
    fun testGetName() {
        val dictionary = NamesDictionaryRepo(432, type = Type.NAME)
        // expected Word(id=432, type=Name)
        val res1 = dictionary.search("A")
        assertEquals(432, res1.id)
        assertEquals("A", res1.name)
        assertEquals(Type.NAME, res1.type)
        // expected Word(id=433, type=Name)
        val res2 = dictionary.search("B")
        assertEquals(433, res2.id)
        assertEquals("B", res2.name)
        assertEquals(Type.NAME, res2.type)
        // expected Word(id=432, type=Name)
        val res3 = dictionary.search("A")
        assertEquals(432, res3.id)
        assertEquals("A", res3.name)
        assertEquals(Type.NAME, res3.type)
    }

    @Test
    fun testExportWordsList() {
        val dictionary = NamesDictionaryRepo(2221, type = Type.NAME)
        dictionary.search("A")
        dictionary.search("B")
        dictionary.search("C")
        dictionary.search("a")
        dictionary.search("B")
        val wordsList = dictionary.exportToWordsList()
        assertEquals(4, wordsList.size)
        assertEquals(2221, wordsList[0].id)
        assertEquals(2223, wordsList[2].id)
    }
}