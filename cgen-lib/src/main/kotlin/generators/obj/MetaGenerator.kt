package generators.obj

import ce.defs.Target
import ce.formatters.CodeStyleRepo
import ce.settings.Project
import generators.obj.input.Block
import generators.obj.input.Node
import generators.obj.out.CommentsBlock
import generators.obj.out.FileData
import generators.obj.out.ProjectOutput
import java.io.File
import java.nio.file.Paths

open class MetaGenerator(
    private val target: Target,
    private val fileGenerator: FileGenerator,
    private val generatorsMap: Map<Class<out Block>, TransformBlockUseCase<out Block>>,
    private val writter: Writter,
    private val prepareFilesListUseCase: PrepareFilesListUseCase,
) {

    private fun translateTree(root: Node, files: Map<String, List<FileData>>) {
        root.subs.forEach {
            if (it is Block) {
                val outputFile = fileGenerator.getBlockFilePath(it)
                val filesData = files[outputFile]!!

//                val namespacePath = it.getParentPath()
                println("Translating ${it.name}")
                if (generatorsMap.contains(it::class.java)) {
                    val generator = generatorsMap.get(it::class.java)!! as TransformBlockUseCase<Block>
                    generator.invoke(filesData, it)
                } else {
                    throw IllegalStateException("${it::class.java} nto supported")
                }
            } else if (it is Node) {
                translateTree(it, files)
            } else {
                error("Unknown leaf ${it}")
            }
        }
    }

    fun translateToOutTree(intree: Node): ProjectOutput {
        val result = ProjectOutput(target)
        val files = prepareFilesListUseCase(intree, result)
        translateTree(intree, files)
        return result
    }


    fun write(projectOutput: ProjectOutput) {
        writter.write(projectOutput)
    }
}