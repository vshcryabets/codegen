package generators.obj

import ce.defs.Target
import ce.defs.globRootNamespace
import ce.settings.Project
import ce.treeio.XmlInTreeWritterImpl
import generators.obj.input.*
import generators.obj.out.ClassData
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File
import java.nio.file.Paths


open class MetaGenerator<T : ClassData>(
    val target: Target = Target.Other,
    val fileGenerator: FileGenerator,
    val generatorsMap: Map<Class<out Block>, Generator<out Block>>,
    private val writter: Writter,
    private val project: Project
) {

    private fun prepareFilesByTree(projectOut: ProjectOutput, root: Node, files: MutableMap<String, List<FileData>>) {
        root.subs.forEach {
            if (it is Block) {
                val outputFile = fileGenerator.getBlockFilePath(it)
                if (!files.contains(outputFile)) {
                    val listOfFiles = fileGenerator.createFile(projectOut, outputFile, it)
                    listOfFiles.forEach { fileData ->
                        projectOut.subs.add(fileData)
                        if (project.addAutogeneratedCommentNotification) {
                            val fileObject = File(outputFile).canonicalFile
                            val pathAbsolute = Paths.get(it.sourceFile)
                            val pathBase = Paths.get(fileObject.absolutePath)
                            val pathRelative = pathBase.relativize(pathAbsolute)
                            fileData.findOrCreateSub(CommentsBlock::class.java).apply {
                                addCommentLine("${fileGenerator.singleComment()} Warning! This file is autogenerated.")
                                addCommentLine("${fileGenerator.singleComment()} Source file $pathRelative")
                            }
                        }
                    }
                    files[outputFile] = listOfFiles
                }
            } else if (it is Node) {
                prepareFilesByTree(projectOut, it, files)
            }
        }
    }

    private fun translateTree(root: Node, files: MutableMap<String, List<FileData>>) {
        root.subs.forEach {
            if (it is Block) {
                val outputFile = fileGenerator.getBlockFilePath(it)
                val filesData = files[outputFile]!!

                val namespacePath = it.getParentPath()
                println("Translating ${it.name}")
                if (generatorsMap.contains(it::class.java)) {
                    val generator = generatorsMap.get(it::class.java)!! as Generator<Block>
                    generator.processBlock(filesData, it)
                }
                else {
//                    val classData = when (it) {
//                        is ConstantsEnum -> enum.processBlock(filesData, it)
//                        is ConstantsBlock -> constantsBlock.processBlock(filesData, it)
//                        is DataClass -> dataClass.processBlock(filesData, it)
//                        else -> null
//                    }
//                    classData?.let {
//                        // TODO check duplicate block in namespace
//                if (namespace.outputBlocks.contains(block.name)) {
//                    throw java.lang.IllegalStateException(
//                        "Duplicate block error! ${namespace.name} already contains block with name ${block.name}"
//                    );
//                }
//                    }
                }

            } else if (it is Node) {
                translateTree(it, files)
            } else {
                error("Unknown leaf ${it}")
            }
        }
    }

    open fun processProject(root: Node, namespaceMap: NamespaceMap): ProjectOutput {
        val result = ProjectOutput(namespaceMap)
        val files = mutableMapOf<String, List<FileData>>()
        prepareFilesByTree(result, root, files)
        translateTree(root, files)

        val treeWritter = XmlInTreeWritterImpl()
        treeWritter.storeTree(project.outputFolder + "${target.name}_output_tree.xml", result)

        return result
    }



    fun write(root: Node, namespaceMap: NamespaceMap) {
        val projectOutput = processProject(root, namespaceMap)
        writter.write(projectOutput)
    }
}