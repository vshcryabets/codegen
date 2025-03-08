package ce.parser.domain.usecase.neuralnetworks

import ce.parser.domain.FormatProject
import ce.parser.domain.dictionaries.tree.Dictionary
import ce.parser.domain.dictionaries.tree.TreeNodeData
import generators.obj.input.Leaf
import generators.obj.input.Node

class TreeToFormatProjectImpl: TreeToFormatProject {

    private fun toVector(leaf: Leaf, vector: MutableList<Int>, map: Dictionary) {
        val nodeId = TreeNodeData.toId(leaf)
        if (!map.map.containsKey(nodeId)) {
            // create new one
            val newData = if (leaf is Node) {
                TreeNodeData(
                    openId = map.maxId++,
                    closeId = map.maxId++
                )
            } else {
                TreeNodeData(openId = map.maxId++, closeId = -1)
            }
            map.map[nodeId] = newData
        }

        val description = map.map[nodeId]!!
        vector.add(description.openId)
        if (leaf is Node) {
            leaf.subs.forEach {
                toVector(it, vector, map)
            }
            vector.add(description.closeId)
        }
    }

    override fun addTree(tree: Leaf,
                         project: FormatProject) : FormatProject {
        val vector = mutableListOf<Int>()
        toVector(leaf = tree,
            vector = vector,
            map = project.dictionary)
        return project.copy(
            samples = project.samples.toMutableList().apply {
                add(vector)
            }
        )
    }

}