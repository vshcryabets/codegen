package ce.entrypoints

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addDatatype
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import kotlin.random.Random
import ce.settings.CodeStyle
import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterJavaUseCaseImpl
import generators.obj.out.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    BuildLtspSampels(args[0], 100).build()
}

class BuildLtspSampels(
    private val outputDir: String,
    private val samplesCount: Int
) {
    private var maxId = 1
    val codeStyleNoSpace = CodeStyle(
        newLinesBeforeClass = 0,
        tabSize = 2,
        preventEmptyBlocks = true,
    )
    private val repoNoSpace = CLikeCodestyleRepo(codeStyleNoSpace)

    data class NodeData(
        val openId: Int,
        val closeId: Int = -1
    )

    fun isUniq(leaf: Leaf): Boolean {
        return when (leaf) {
            is VariableName -> true
            is Keyword -> true
            else -> false
        }
    }

    fun toId(leaf: Leaf): String {
        if (isUniq(leaf)) {
            return leaf.javaClass.simpleName + "_" + leaf.name
        }
        return leaf.javaClass.simpleName
    }

    fun buildTree(rnd: Random): Node {
        val fieldsCount = rnd.nextInt(1,3)
        val input = RegionImpl().apply {
            addOutBlock("record TEST") {
                addSub(OutBlockArguments().apply {
                    for (j in 0..fieldsCount) {
                        addSub(ArgumentNode()).apply {
                            addDatatype("int")
                            addVarName("A$j")
                        }
                    }
                })
            }
        }
        return input
    }

    fun toVector(leaf : Leaf, vector: MutableList<Int>, map: MutableMap<String, NodeData>) {
        val nodeId = toId(leaf)
        if (!map.containsKey(nodeId)) {
            // create new one
            val newData = if (leaf is Node) {
                NodeData(
                    openId = maxId++,
                    closeId = maxId++
                )
            } else {
                NodeData(openId = maxId++, closeId = -1)
            }
            map[nodeId] = newData
        }

        val description = map[nodeId]!!
        vector.add(description.openId)
        if (leaf is Node) {
            leaf.subs.forEach {
                toVector(it, vector, map)
            }
            vector.add(description.closeId)
        }
    }

    fun build() {
        val rnd = Random(System.currentTimeMillis())
        val map = mutableMapOf<String, NodeData>()
        val formatter = CodeFormatterJavaUseCaseImpl(repoNoSpace)
        val outS1File = File(outputDir, "outs1.csv")
        val outS2File = File(outputDir, "outs2.csv")
        val outS1 = OutputStreamWriter(FileOutputStream(outS1File))
        val outS2 = OutputStreamWriter(FileOutputStream(outS2File))

        for (i in 0..samplesCount) {
            val vector = mutableListOf<Int>()
            val input = buildTree(rnd)
            toVector(input, vector, map)
            vector.forEachIndexed { index, it ->
                if (index > 0)
                    outS1.write(",")
                outS1.write("$it")
            }
            outS1.write("\n")
            val output = formatter.invoke(input)
            vector.clear()
            toVector(output, vector, map)
            vector.forEachIndexed { index, it ->
                if (index > 0)
                    outS2.write(",")
                outS2.write("$it")
            }
            outS2.write("\n")
        }
        outS1.close()
        outS2.close()
        println(map)
    }
}