package generators.swift

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.FileMetaInformation
import generators.obj.syntaxParseTree.PackageDirectory

class SwiftFileGenerator() : CLikeFileGenerator() {
    override fun createFile(
        project: OutputTree,
        workingDirectory: String,
        packageDirectory: String,
        outputFile: String,
        block: Block
    ): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
            addSub(FileMetaInformation(workingDirectory))
        })
    }
}