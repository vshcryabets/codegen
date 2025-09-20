package generators.swift

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.OutputTree

class SwiftFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
        })
    }
}