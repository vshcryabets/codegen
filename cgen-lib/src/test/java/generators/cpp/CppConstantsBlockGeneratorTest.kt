package generators.cpp

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddBlockDefaultsUseCase
import generators.obj.input.Block
import generators.obj.input.ConstantsBlock
import generators.obj.input.Namespace
import generators.obj.input.TreeRoot
import generators.obj.out.ClassData
import generators.obj.out.NamespaceBlock
import generators.obj.out.ProjectOutput
import org.gradle.internal.impldep.org.junit.Assert
import org.junit.jupiter.api.Test

class CppConstantsBlockGeneratorTest {

    @Test
    fun test() {
        val project = ProjectOutput(Target.Cxx)
        val item = CppConstantsBlockGenerator(
            addBlockDefaultsUseCase = object: AddBlockDefaultsUseCase {
                override fun invoke(desc: Block, result: ClassData) {

                }
            }
        )
        val headerFile = CppHeaderFile("a", project)
        val cxxFile = CppFileData("b", project)
        val files = listOf(headerFile, cxxFile)
        val namespace = Namespace("a", TreeRoot)
        val block = namespace.addSub(ConstantsBlock("c")).apply {
            defaultType(DataType.int32)
            add("A", 1)
            add("B")
            add("C")
        }
        Assert.assertFalse("Dirty flag should be false in .h before changes", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false in .cpp before changes", cxxFile.isDirty)
        item(files, block)
        Assert.assertTrue("Dirty flag should be true", headerFile.isDirty)
        Assert.assertFalse("Dirty flag should be false", cxxFile.isDirty)
        val outNamespace = headerFile.findOrNull(NamespaceBlock::class.java)!!
        Assert.assertEquals(1, outNamespace.subs.size)
        val constantsBlock = outNamespace.findOrNull(CppClassData::class.java)!!
        Assert.assertEquals(2, constantsBlock.subs.size)
    }
}