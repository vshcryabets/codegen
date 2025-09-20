package generators.cpp

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.FileMetaInformation

class CppFileGenerator() : CLikeFileGenerator() {
    override fun createFile(
        project: OutputTree,
        workingDirectory: String,
        packageDirectory: String,
        outputFile: String,
        block: Block
    ): List<FileData> {
        return listOf(
            CppFileData(outputFile + ".cpp").apply {
                setParent2(project)
                addSub(FileMetaInformation(workingDirectory))
                addSub(ImportsBlock())
                isDirty = false
            },
            CppHeaderFile(outputFile + ".h").apply {
                setParent2(project)
                addSub(FileMetaInformation(workingDirectory))
                addSub(CompilerDirective("pragma once"))
                addSub(ImportsBlock())
                isDirty = false
            }
        )
    }
}