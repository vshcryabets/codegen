package generators.obj

import ce.defs.Target
import ce.settings.Project
import generators.obj.input.*
import generators.obj.out.ClassData
import generators.obj.out.FileData
import generators.obj.out.OutNamespace
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

    private fun prepareFilesByTree(projectOut: ProjectOutput, root: Node, files: MutableMap<String, FileData>) {
        root.subs.forEach {
            if (it is Block) {
                val outputFile = fileGenerator.getBlockFilePath(it)
                if (!files.contains(outputFile)) {
                    val data = fileGenerator.createFile(projectOut, outputFile)
                    if (project.addAutogeneratedCommentNotification) {
                        val fileObject = File(outputFile).canonicalFile
                        val pathAbsolute = Paths.get(it.sourceFile)
                        val pathBase = Paths.get(fileObject.absolutePath)
                        val pathRelative = pathBase.relativize(pathAbsolute)
                        fileGenerator.appendInitalComment(data, "Warning! This file is autogenerated.")
                        fileGenerator.appendInitalComment(data, "Source file $pathRelative")
                    }
                    files[outputFile] = data
                }
            } else if (it is Node) {
                prepareFilesByTree(projectOut, it, files)
            }
        }
    }

    private fun translateTree(root: Node, files: MutableMap<String, FileData>) {
        root.subs.forEach {
            if (it is Block) {
                val outputFile = fileGenerator.getBlockFilePath(it)
                val fileData = files[outputFile]!!

                val namespacePath = it.getParentPath()
                if (!fileData.namespaces.contains(namespacePath)) {
                    fileData.namespaces.put(namespacePath, OutNamespace(namespacePath))
                }
                val namespace = fileData.namespaces[namespacePath]!!
            }
        }
    }


    open fun processProject(root: Node, namespaceMap: NamespaceMap): ProjectOutput {
        val result = ProjectOutput(namespaceMap)
        val files = result.files

        // prepare output files
        prepareFilesByTree(root, files)
        println(files)
        translateTree(root, files)

//        fileBlockMap.forEach {
//            it.value.forEach { block ->

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


    fun write(root: Node, namespaceMap: NamespaceMap) {
        val projectOutput = processProject(root, namespaceMap)
        writter.write(projectOutput)
    }
}