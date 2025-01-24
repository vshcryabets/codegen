package ce.entrypoints

import generators.obj.input.Leaf
import generators.obj.input.Node
import generators.obj.input.addDatatype
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.OutBlockArguments
import generators.obj.out.RegionImpl
import generators.obj.out.VariableName
import kotlin.random.Random

fun main(args: Array<String>) {
    BuildLtspSampels(args[0], 1).build()
}

class BuildLtspSampels(
    private val outputDir: String,
    private val samplesCount: Int
) {
    private var maxId = 1

    data class NodeData(
        val openId: Int,
        val closeId: Int = -1
    )

    fun isUniq(leaf: Leaf): Boolean {
        return when (leaf) {
            is VariableName -> true
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
        val fieldsCount = rnd.nextInt(3,6)
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
        for (i in 0..samplesCount) {
            val vector = mutableListOf<Int>()
            val input = buildTree(rnd)
            toVector(input, vector, map)
            println(vector)
        }
        println(map)
    }
}