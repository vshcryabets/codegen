package generators.kotlin

import ce.defs.Target
import generators.obj.input.DataClass
import generators.obj.input.NamespaceImpl
import generators.obj.input.addSub
import generators.obj.out.OutputTree
import org.junit.jupiter.api.Assertions.assertEquals
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

        assertEquals(1, result.size)
//        assertEquals("outputFile", result[0].)
    }

}