package ce.formatters.cpp

import ce.defs.DataType
import ce.defs.Target
import ce.domain.usecase.add.AddRegionDefaultsUseCaseImpl
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterUseCaseImpl
import ce.settings.CodeStyle
import generators.cpp.CompilerDirective
import generators.cpp.CppConstantsBlockGenerator
import generators.cpp.CppFileGenerator
import generators.cpp.CppHeaderFile
import generators.cpp.GetArrayDataTypeUseCase
import generators.cpp.GetTypeNameUseCase
import generators.cpp.PrepareRightValueUseCase
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.NamespaceImpl
import generators.obj.abstractSyntaxTree.TreeRoot
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.FileMetaInformation
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.NlSeparator
import generators.obj.syntaxParseTree.OutputTree
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FormatInclude {
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)
    val formatter = CodeFormatterUseCaseImpl(repoNoSpace)
    val fileGenerator = CppFileGenerator()
    private val arrayDataType = GetArrayDataTypeUseCase()
    private val getTypeNameUseCase = GetTypeNameUseCase(arrayDataType)
    val prepareRightValueUseCase = PrepareRightValueUseCase(getTypeNameUseCase)
    val cppConstantsBlockGenerator = CppConstantsBlockGenerator(
        addBlockDefaultsUseCase = AddRegionDefaultsUseCaseImpl(repoNoSpace),
        prepareRightValueUseCase = prepareRightValueUseCase
    )

    @Test
    fun testCompilerDirectivesFormatting() {
        val headerFile = FileDataImpl("a")
        headerFile.addSub(CompilerDirective("pragma once"))
        headerFile.addSub(CompilerDirective("pragma once"))
        headerFile.addSub(CompilerDirective("pragma once"))
        val output = formatter(headerFile)
        // expected output
        // <FileDataImpl>
        //     <pragma once>
        //     <nl>
        //     <pragma once>
        //     <nl>
        //     <pragma once>
        //     <nl>
        // </FileDataImpl>
        Assertions.assertEquals(6, output.subs.size)
    }

    @Test
    fun testIncludeFormatting() {
        val projectOutput = OutputTree(Target.Cpp)

        val namespace = NamespaceImpl("a").apply { setParent2(TreeRoot) }
        val block = namespace.addSub(ConstantsBlock("c")).apply {
            addBlockComment("182TEST_COMMENT")
            defaultType(DataType.int32)
            add("A", 1)
            add("B")
            add("C")
        }

        val files = fileGenerator.createFile(
            projectOutput,
            workingDirectory = "./",
            packageDirectory = "",
            "a", block)
        val headerFile = files.first { it is CppHeaderFile } as CppHeaderFile

        cppConstantsBlockGenerator(files, block)

        val output = formatter(headerFile)
        // expected output
        // <CppHeaderFile>
        //     <FileMetaInformation ./>
        //     <pragma once>
        //     <nl>
        //     <ImportsBlock> <"<cstdint>"> <nl> </ImportsBlock>
        //     <NamespaceBlock> ... </NamespaceBlock>
        //     <nl>
        // </CppHeaderFile>
        Assertions.assertEquals(CppHeaderFile::class.java, output.javaClass)
        Assertions.assertEquals(6, output.subs.size)
        Assertions.assertEquals(FileMetaInformation::class.java, output.subs[0]::class.java)
        Assertions.assertEquals(CompilerDirective::class.java, output.subs[1]::class.java)
        Assertions.assertEquals(NlSeparator::class.java, output.subs[2]::class.java)
        Assertions.assertEquals(ImportsBlock::class.java, output.subs[3]::class.java)
        Assertions.assertEquals(NamespaceBlock::class.java, output.subs[4]::class.java)
        Assertions.assertEquals(NlSeparator::class.java, output.subs[5]::class.java)

        val importsBlock = output.subs[3] as ImportsBlock
        Assertions.assertEquals(2, importsBlock.subs.size)

    }
}