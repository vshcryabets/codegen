package generators.swift

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.FileGenerator
import generators.obj.input.Block
import generators.obj.out.FileData
import java.io.File

class SwiftFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(outputFile: String): FileData {
        return FileData().apply {
            this.fullOutputFileName= outputFile
        }
    }
}