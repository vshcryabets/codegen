package generators.java

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.NamespaceDeclaration
import generators.obj.out.ProjectOutput
import java.io.File

class JavaFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(project: ProjectOutput, outputFile: String, block: Block): List<FileData> {
        return listOf(FileData(outputFile, project).apply {
            addSub(NamespaceDeclaration(block.getParentPath()))
        })
    }

    override fun getBlockFilePath(block: Block): String {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
        val namespace = block.getParentPath().replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + namespace + File.separatorChar + fileName
    }
}