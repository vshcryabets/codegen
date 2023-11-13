package generators.swift

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import generators.obj.out.FileDataImpl
import generators.obj.out.ProjectOutput

class SwiftFileGenerator() : CLikeFileGenerator() {
    override fun createFile(project: ProjectOutput, outputFile: String, block: Block): List<FileData> {
        return listOf(FileDataImpl(outputFile).apply {
            setParent2(project)
        })
    }
}