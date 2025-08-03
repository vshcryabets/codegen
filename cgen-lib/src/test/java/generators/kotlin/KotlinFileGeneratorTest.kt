package generators.kotlin

import ce.defs.Target
import generators.obj.input.DataClass
import generators.obj.input.NamespaceImpl
import generators.obj.input.addSub
import generators.obj.out.FileData
import generators.obj.out.Keyword
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.OutputTree
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class KotlinFileGeneratorTest {

    @Test
    fun createFileGeneratesCorrectFileData() {
        val generator = KotlinFileGenerator()
        val project = OutputTree(
            target = Target.Kotlin,
        )
        val namespace = "com.example.test"
        val rootNs = NamespaceImpl("")
        val workNs = rootNs.getNamespace(namespace)
        project.addSub(rootNs)
        val block = DataClass(
            name = "TestBlock",
            outputFile = "testOutputFile")
        workNs.addSub(block)
        val result = generator.createFile(project, "testOutputFile", block)

        // expected result
        // <FileData>
        //      <NamespaceDeclaration>
        //          <keyword>package</keyword>
        //          <VariableName>com.example.test</VariableName>
        //      </NamespaceDeclaration>
        assertEquals(1, result.size)
        assertTrue(result[0] is FileData)
        val fileData = result[0] as FileData
        assertEquals(NamespaceDeclaration::class.java, fileData.subs[0].javaClass)
        val nsDeclaration = fileData.subs[0] as NamespaceDeclaration
        assertEquals(2, nsDeclaration.subs.size)
        assertEquals(Keyword::class.java, nsDeclaration.subs[0].javaClass)
        assertEquals("package", (nsDeclaration.subs[0] as Keyword).name)
        assertEquals("com.example.test", nsDeclaration.subs[1].name)
    }

}