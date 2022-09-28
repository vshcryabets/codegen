package generators.cpp

import generators.obj.FileGenerator
import generators.obj.Writter
import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.out.FileData
import java.io.BufferedWriter
import java.io.File

class CppWritter(fileGenerator: FileGenerator, outputFolder: String)
    : Writter(fileGenerator, fileGenerator.style, outputFolder) {

//    fun writeHeaderFile(node: CppHeaderPart) {
//        // try to find FileData
//        var fileData : FileData? = null
//        var parent : Node = node
//        while (parent.parent != null) {
//            parent = parent.parent!!
//            if (parent is FileData) {
//                fileData = parent
//                break
//            }
//        }
//        if (fileData == null) {
//            throw java.lang.IllegalStateException("No fileData for Header file")
//        }
//        var outputFile = File(fileData.name + ".h")
//        outputFile.parentFile.mkdirs()
//        println("Writing headers $outputFile")
//        outputFile.bufferedWriter().use { out ->
//            writeSubNodes(node, out)
//        }
//    }

    override fun writeLeaf(leaf: Leaf, out: BufferedWriter) {
        when (leaf) {
            is CompilerDirective -> out.write("#${leaf.name}${fileGenerator.newLine()}")
            else -> super.writeLeaf(leaf, out)
        }
    }

    override fun writeNode(node: Node, out: BufferedWriter) {
        when (node) {
            is CppHeaderFile -> {
                super.writeNode(node, out)
            }
            else -> super.writeNode(node, out)
        }
    }

    override fun writeFile(fileData: FileData) {
        val cppFileData = fileData as CppFileData
        var outputFile = File(fileData.name + ".cpp")
        var outputHeaderFile = File(fileData.name + ".h")
        outputFile.parentFile.mkdirs()
        println("Writing $outputFile")
        println("Writing $outputHeaderFile")
        outputHeaderFile.bufferedWriter().use { headerOut->
            outputFile.bufferedWriter().use { out ->
                writeNode(fileData, out)
            }
        }
    }
}