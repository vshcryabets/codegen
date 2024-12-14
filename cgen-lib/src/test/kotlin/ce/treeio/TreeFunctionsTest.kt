package ce.treeio

import generators.obj.input.NamespaceImpl
import generators.obj.input.Node
import org.junit.jupiter.api.Assertions.*

class TreeFunctionsTest {
    val reader = XmlTreeReader()

    @org.junit.jupiter.api.Test
    fun mergeTrees() {
        val tree1 = reader.loadFromString("""
            <Namespace name="com.goldman.xml">
                    <ConstantsBlock defaultType="int32" name="Constants1">
                        <ConstantDesc name="OREAD"/>
                        <ConstantDesc name="OWRITE"/>
                    </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        val tree2 = reader.loadFromString("""
            <Namespace name="com.goldman.xml2">
                    <ConstantsBlock defaultType="int32" name="Constants2">
                        <ConstantDesc name="OREAD"/>
                        <ConstantDesc name="OWRITE"/>
                    </ConstantsBlock>
            </Namespace>
        """.trimIndent())
        val resultTree = TreeFunctions.mergeTrees(tree1 as Node, tree2 as Node)
        assertTrue(resultTree is NamespaceImpl)
        val rootNs = resultTree as NamespaceImpl
        assertEquals("com", rootNs.name)
        val ns1 = rootNs.getNamespace("goldman.xml")
        val ns2 = rootNs.getNamespace("goldman.xml2")
        assertEquals(1, ns1.subs.size)
        assertEquals(1, ns2.subs.size)
    }

    @org.junit.jupiter.api.Test
    fun mergeTreesWrongRootTypes() {
        val tree1 = reader.loadFromString("""
            <Namespace name="com.goldman.xml" />
        """.trimIndent())
        val tree2 = reader.loadFromString("""
            <ConstantsBlock defaultType="int32" name="Constants2"></ConstantsBlock>
        """.trimIndent())
        try {
            TreeFunctions.mergeTrees(tree1 as Node, tree2 as Node)
            assertTrue(false)
        } catch (error: TreeMergeWrongRoot) {

        }
    }

    @org.junit.jupiter.api.Test
    fun mergeTreesWrongRootNames() {
        val tree1 = reader.loadFromString("""
            <Namespace name="com.goldman.xml" />
        """.trimIndent())
        val tree2 = reader.loadFromString("""
            <Namespace name="org.goldman.xml" />
        """.trimIndent())
        try {
            TreeFunctions.mergeTrees(tree1 as Node, tree2 as Node)
            assertTrue(false)
        } catch (error: TreeMergeWrongRootNames) {

        }
    }
}