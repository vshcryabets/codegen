package generators.swift

import ce.settings.CodeStyle
import generators.obj.CLikeFileGenerator
import generators.obj.FileGenerator
import generators.obj.out.FileData

class SwiftFileGenerator(style: CodeStyle) : CLikeFileGenerator(style) {
    override fun createFile(outputFile: String): FileData {
        return FileData().apply {
            this.fullOutputFileName= outputFile
        }
    }


}