package generators.cpp

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.OutputTree

class CppFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData> {
        return listOf(
            CppFileData(outputFile + ".cpp").apply {
                setParent2(project)
            },
            CppHeaderFile(outputFile + ".h").apply {
                setParent2(project)
            }
        )
    }
}