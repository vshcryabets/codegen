package ce.parser

import ce.formatters.CLikeCodestyleRepo
import ce.formatters.CodeFormatterJavaUseCaseImpl
import ce.parser.domain.FormatProject
import ce.parser.domain.usecase.LoadTreeDictionaryFromJson
import ce.parser.domain.usecase.neuralnetworks.TreeToFormatProjectImpl
import ce.parser.domain.usecase.save.SaveTreeDictrionaryToJson
import ce.parser.domain.usecase.save.StoreFormatProjectToJsonImpl
import ce.settings.CodeStyle
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import generators.obj.input.Node
import generators.obj.input.addDatatype
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.OutBlockArguments
import generators.obj.out.RegionImpl
import java.io.File
import kotlin.random.Random

fun main(args: Array<String>) {
    BuildLtspSampels(args[0], 1000).build()
}

class BuildLtspSampels(
    private val outputDir: String,
    private val samplesCount: Int
) {
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

    fun build() {
        val rnd = Random(System.currentTimeMillis())
        val dictionaryFileName = "dictionary.json"
        val objectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        val loadDictionary = LoadTreeDictionaryFromJson(objectMapper)
        val treeToNNProject = TreeToFormatProjectImpl()

        val dict = loadDictionary.load(File(outputDir, dictionaryFileName))

        val storeFormatProject = StoreFormatProjectToJsonImpl(objectMapper)
        val saveTreeDictionary = SaveTreeDictrionaryToJson()
        val formatter = CodeFormatterJavaUseCaseImpl(repoNoSpace)

        var trainingFormatProject = FormatProject(
            name = "Training data",
            type = "RNNTrainingData",
            process = "samples_builder",
            dictionary = dict,
            samples = emptyList(),
            author = System.getProperty("user.name")
        )

        var sourceProject = FormatProject(
            name = "Format sources",
            type = "RNNSources",
            process = "samples_builder",
            dictionary = dict,
            samples = emptyList(),
            author = System.getProperty("user.name")
        )

        for (i in 0..samplesCount) {
            val input = buildTree(rnd)
            sourceProject = treeToNNProject.addTree(input, sourceProject)

            val output = formatter(input)
            trainingFormatProject = treeToNNProject.addTree(output, trainingFormatProject)

            trainingFormatProject.samples.last().filter { it !in sourceProject.samples.last() }.toSet().forEach { id ->
                dict.map.filter { it.value.openId == id || it.value.closeId == id }.forEach {
                    dict.map[it.key] = it.value.copy(priority = 1)
                }
            }
        }
        storeFormatProject.store(
            trainingFormatProject,
            File(outputDir, "training.json")
        )
        storeFormatProject.store(
            sourceProject,
            File(outputDir, "sources.json")
        )
        saveTreeDictionary.save(File(outputDir, dictionaryFileName), dict)
    }
}