package generators.kotlin

import ce.defs.Target
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NamespaceDeclaration
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.FileMetaInformation
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
        val result = generator.createFile(project,
            workingDirectory = "./",
            packageDirectory = "",
            "testOutputFile", block)

        // expected result
        // <FileData>
        //      <FileMetaInformation>
        //      <NamespaceDeclaration>
        //          <keyword>package</keyword>
        //          <VariableName>com.example.test</VariableName>
        //      </NamespaceDeclaration>
        assertEquals(1, result.size)
        assertTrue(result[0] is FileData)
        val fileData = result[0] as FileData
        assertEquals(FileMetaInformation::class.java, fileData.subs[0].javaClass)
        assertEquals(NamespaceDeclaration::class.java, fileData.subs[1].javaClass)
        val nsDeclaration = fileData.subs[1] as NamespaceDeclaration
        assertEquals(2, nsDeclaration.subs.size)
        assertEquals(Keyword::class.java, nsDeclaration.subs[0].javaClass)
        assertEquals("package", (nsDeclaration.subs[0] as Keyword).name)
        assertEquals("com.example.test", nsDeclaration.subs[1].name)
    }

}