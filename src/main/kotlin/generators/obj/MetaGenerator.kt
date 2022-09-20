package generators.obj

import ce.defs.Target
import ce.settings.Project
import generators.obj.input.*
import generators.obj.out.ClassData
import generators.obj.out.FileData
import generators.obj.out.Namespace
import generators.obj.out.ProjectOutput
import java.io.File
import java.nio.file.Paths


open class MetaGenerator<T : ClassData>(
    val target: Target = Target.Other,
    val enum: Generator<ConstantsEnum, T>,
    val constantsBlock: Generator<ConstantsBlock, T>,
    val dataClass: Generator<DataClass, T>,
    val fileGenerator: FileGenerator,
    private val writter: Writter,
    private val project: Project
) {

    open fun processProject(root: InNode, namespaceMap: NamespaceMap): ProjectOutput {
        val result = ProjectOutput(namespaceMap)
        val files = result.files
        val fileBlockMap = mutableMapOf<FileData, MutableList<Block>>()

        // prepare output files
//        root.subs.forEach {
//            val outputFile = fileGenerator.getBlockFilePath(it)
//            val fileData = if (files.contains(outputFile)) {
//                files[outputFile]!!
//            } else {
//                val data = fileGenerator.createFile(outputFile)
//
//                if (project.addAutogeneratedCommentNotification) {
//                    val fileObject = File(outputFile).canonicalFile
//                    val pathAbsolute = Paths.get(it.sourceFile)
//                    val pathBase = Paths.get(fileObject.absolutePath)
//                    val pathRelative = pathBase.relativize(pathAbsolute)
//                    fileGenerator.appendInitalComment(data, "Warning! This file is autogenerated.")
//                    fileGenerator.appendInitalComment(data, "Source file $pathRelative")
//                }
//
//                files[outputFile] = data
//                fileBlockMap[data] = mutableListOf()
//                data
//            }
//            fileBlockMap[fileData]!!.add(it)
//        }
//        println(fileBlockMap)
//
//        fileBlockMap.forEach {
//            val fileData = it.key
//            it.value.forEach { block ->
//                val namespacePath = block.getParentPath()
//                if (!fileData.namespaces.contains(namespacePath)) {
//                    fileData.namespaces.put(namespacePath, Namespace(namespacePath))
//                }
//                val namespace = fileData.namespaces[namespacePath]!!
//                val classData = when (block) {
//                    is ConstantsEnum -> enum.processBlock(fileData, block)
//                    is ConstantsBlock -> constantsBlock.processBlock(fileData, block)
//                    is DataClass -> dataClass.processBlock(fileData, block)
//                    else -> ClassData.emptyClassData
//                }
//                if (namespace.outputBlocks.contains(block.name)) {
//                    throw java.lang.IllegalStateException(
//                        "Duplicate block error! ${namespace.name} already contains block with name ${block.name}"
//                    );
//                }
//                namespace.outputBlocks[block.name] = classData
//            }
//        }

        return result
    }

    fun write(root: InNode, namespaceMap: NamespaceMap) {
        val projectOutput = processProject(root, namespaceMap)
        writter.write(projectOutput)
    }
}