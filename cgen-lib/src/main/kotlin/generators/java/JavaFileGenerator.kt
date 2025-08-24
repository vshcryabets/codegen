package generators.java

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.NamespaceDeclaration
import generators.obj.syntaxParseTree.OutputTree
import java.io.File

class JavaFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: OutputTree, outputFile: String, block: Block): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
            addSub(NamespaceDeclaration(block.getParentPath()))
            addSub(ImportsBlock())
        })
    }

    override fun getBlockFilePath(block: Block): String {
        var fileName = block.name
        if (block.outputFile.isNotEmpty()) {
            fileName = block.outputFile
        }
        val namespace = block.getParentPath().replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
    }
}