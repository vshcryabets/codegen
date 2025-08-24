package generators.obj

import generators.obj.abstractSyntaxTree.Block
import java.io.File

abstract class CLikeFileGenerator() : FileGenerator() {

    override fun getBlockFilePath(block: Block): String {
        var fileName = "${block.name}"
        if (block.outputFile.isNotEmpty()) {
            fileName = "${block.outputFile}"
        }
//        val namespace = block.namespace.replace('.', File.separatorChar)
        return block.objectBaseFolder + File.separatorChar + fileName
    }
}