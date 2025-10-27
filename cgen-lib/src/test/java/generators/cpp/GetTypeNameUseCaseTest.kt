package generators.cpp

import ce.defs.DataType
import generators.obj.abstractSyntaxTree.findOrCreateSub
import generators.obj.syntaxParseTree.ImportLeaf
import generators.obj.syntaxParseTree.ImportsBlock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetTypeNameUseCaseTest {
    val getArrayDataTypeUseCase = GetArrayDataTypeUseCase()
    val useCase = GetTypeNameUseCase(getArrayDataTypeUseCase)
    @Test
    fun testStringInclude() {
        val filedata = CppHeaderFile()
        val imports = filedata.findOrCreateSub(ImportsBlock::class.java)
        useCase.typeTo(imports, DataType.string)
        Assertions.assertTrue(imports.subs.size > 0)
        Assertions.assertEquals(ImportLeaf::class.java, imports.subs[0]::class.java)
        Assertions.assertEquals("<string>", imports.subs[0].name)
    }

    @Test
    fun testUintInclude() {
        val filedata = CppHeaderFile()
        val imports = filedata.findOrCreateSub(ImportsBlock::class.java)
        useCase.typeTo(imports, DataType.uint32)
        Assertions.assertTrue(imports.subs.size > 0)
        Assertions.assertEquals(ImportLeaf::class.java, imports.subs[0]::class.java)
        Assertions.assertEquals("<cstdint>", imports.subs[0].name)
    }

    @Test
    fun testVectorInclude() {
        val filedata = CppHeaderFile()
        val imports = filedata.findOrCreateSub(ImportsBlock::class.java)
        useCase.typeTo(imports, DataType.array(DataType.bool))
        Assertions.assertTrue(imports.subs.size > 0)
        Assertions.assertEquals(ImportLeaf::class.java, imports.subs[0]::class.java)
        Assertions.assertEquals("<vector>", imports.subs[0].name)
    }

    @Test
    fun testOptionalInclude() {
        val filedata = CppHeaderFile()
        val imports = filedata.findOrCreateSub(ImportsBlock::class.java)
        useCase.typeTo(imports, DataType.boolNullable)
        Assertions.assertTrue(imports.subs.size > 0)
        Assertions.assertEquals(ImportLeaf::class.java, imports.subs[0]::class.java)
        Assertions.assertEquals("<optional>", imports.subs[0].name)
    }
}