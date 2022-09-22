package generators.swift

import ce.settings.CodeStyle
import com.sun.source.tree.Tree
import generators.obj.CLikeFileGenerator
import generators.obj.FileGenerator
import generators.obj.input.Block
import generators.obj.input.TreeRoot
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File

class SwiftFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(project: ProjectOutput, outputFile: String): FileData {
        return FileData(outputFile, project)
    }
}