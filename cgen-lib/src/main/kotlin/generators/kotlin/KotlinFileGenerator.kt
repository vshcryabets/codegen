package generators.kotlin

import generators.obj.CLikeFileGenerator
import generators.obj.abstractSyntaxTree.Block
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.FileDataImpl
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.Keyword
import generators.obj.syntaxParseTree.NamespaceDeclaration
import generators.obj.syntaxParseTree.OutputTree
import generators.obj.syntaxParseTree.VariableName
import generators.obj.syntaxParseTree.FileMetaInformation
import generators.obj.syntaxParseTree.PackageDirectory
import generators.obj.syntaxParseTree.WorkingDirectory
import java.io.File

class KotlinFileGenerator() : CLikeFileGenerator() {
    override fun createFile(
        project: OutputTree,
        workingDirectory: String,
        packageDirectory: String,
        outputFile: String,
        block: Block
    ): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
            addSub(FileMetaInformation("").apply {
                addSub(WorkingDirectory(workingDirectory))
                addSub(PackageDirectory(packageDirectory))
            })
            addSub(NamespaceDeclaration("").apply {
                addSub(Keyword("package"))
                addSub(VariableName(block.getParentPath()))
            })
            addSub(ImportsBlock())
        })
    }

    override fun getBlockFilePath(block: Block): BlockPath {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
        val namespace = block.getParentPath().replace('.', File.separatorChar)
        return BlockPath(
            baseObjectDirecotry = block.objectBaseFolder,
            namespacePath = namespace,
            fileName = fileName
        )
    }
}