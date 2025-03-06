package ce.parser

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
import ce.parser.domain.dictionaries.FormatProject
import ce.parser.domain.dictionaries.TreeNodeData
import ce.parser.domain.usecase.LoadTreeDictionaryFromJson
import ce.parser.domain.usecase.SaveTreeDictrionaryToJson
import ce.parser.domain.usecase.StoreFormatProjectToJsonImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import generators.obj.out.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.swing.tree.TreeNode

fun main(args: Array<String>) {
    BuildLtspSampels(args[0], 1000).build()
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

    fun toVector(leaf : Leaf, vector: MutableList<Int>, map: MutableMap<String, TreeNodeData>) {
        val nodeId = TreeNodeData.toId(leaf)
        if (!map.containsKey(nodeId)) {
            // create new one
            val newData = if (leaf is Node) {
                TreeNodeData(
                    openId = maxId++,
                    closeId = maxId++
                )
            } else {
                TreeNodeData(openId = maxId++, closeId = -1)
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

    private fun computeDifference(srcVector: List<Int>, dstVector: List<Int>): Set<Int> {
        return dstVector.filter { it !in srcVector }.toSet()
    }

    fun build() {
        val rnd = Random(System.currentTimeMillis())
        val dictionaryFileName = "dictionary.json"
        val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        val map = mutableMapOf<String, TreeNodeData>()
        val loadMapFromJsonFile = LoadTreeDictionaryFromJson(objectMapper)
        val storeFormatProject = StoreFormatProjectToJsonImpl(objectMapper)
        val saveTreeDictionary = SaveTreeDictrionaryToJson()
        loadMapFromJsonFile.load(File(outputDir, dictionaryFileName)).forEach { (key, value) ->
            maxId = maxOf(maxId, value.openId, value.closeId)
            map[key] = value
        }
        val formatter = CodeFormatterJavaUseCaseImpl(repoNoSpace)
        val dstVectors = mutableListOf<List<Int>>()
        val srcVectors = mutableListOf<List<Int>>()

        for (i in 0..samplesCount) {
            val input = buildTree(rnd)
            val srcVector = mutableListOf<Int>()
            toVector(input, srcVector, map)
            srcVectors.add(srcVector)
            val output = formatter(input)
            val dstVector = mutableListOf<Int>()
            toVector(output, dstVector, map)
            computeDifference(srcVector, dstVector).forEach { id ->
                map.filter { it.value.openId == id || it.value.closeId == id }.forEach {
                    map[it.key] = it.value.copy(priority = 1)
                }
            }
            dstVectors.add(dstVector)
        }
        storeFormatProject.store(
            FormatProject(
                name = "RNNTrainingData",
                process = "samples_builder",
                dictionary = map,
                samples = dstVectors
            ),
            File(outputDir, "training.json")
        )
        storeFormatProject.store(
            FormatProject(
                name = "RNNSources",
                process = "samples_builder",
                dictionary = map,
                samples = srcVectors
            ),
            File(outputDir, "sources.json")
        )
        saveTreeDictionary.save(File(outputDir, dictionaryFileName), map)
        println(map)
    }

    private fun writeVectorToCsv(vector: Iterable<Int>, writer: OutputStreamWriter) {
        vector.forEachIndexed { index, it ->
            if (index > 0)
                writer.write(",")
            writer.write("$it")
        }
        writer.write("\n")
    }
}