package generators.obj

import ce.defs.Target
import ce.formatters.CodeFormatterUseCase
import generators.obj.input.Block
import generators.obj.input.Node
import generators.obj.input.addSub
import generators.obj.out.CodeStyleOutputTree
import generators.obj.out.FileData
import generators.obj.out.OutputTree

open class MetaGenerator(
    private val target: Target,
    private val fileGenerator: FileGenerator,
    private val generatorsMap: Map<Class<out Block>, TransformBlockUseCase<out Block>>,
    private val prepareFilesListUseCase: PrepareFilesListUseCase,
    private val codeFormatter: CodeFormatterUseCase,
) {

    private fun translateTree(root: Node, files: Map<String, List<FileData>>) {
        root.subs.forEach {
            if (it is Block) {
                val outputFile = fileGenerator.getBlockFilePath(it)
                val filesData = files[outputFile]!!

                println("Translating ${it.name}")
                if (generatorsMap.contains(it::class.java)) {
                    val generator = generatorsMap.get(it::class.java)!! as TransformBlockUseCase<Block>
                    generator.invoke(filesData, it)
                } else {
                    throw IllegalStateException("${it::class.java} not supported")
                }
            } else if (it is Node) {
                translateTree(it, files)
            } else {
                error("Unknown leaf ${it}")
            }
        }
    }

    fun translateToOutTree(intree: Node): OutputTree {
        val result = OutputTree(target)
        val files = prepareFilesListUseCase(intree, result)
        translateTree(intree, files)
        return result
    }

    fun prepareCodeStyleTree(projectOutput: OutputTree): CodeStyleOutputTree {
        val tree = codeFormatter(projectOutput)
        val result = CodeStyleOutputTree(
            target = projectOutput.target
        )
        tree.subs.forEach {
            result.addSub(it)
        }
        return result
    }
}