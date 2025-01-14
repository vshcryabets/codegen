package ce.entrypoints

import generators.obj.input.addDatatype
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.OutBlockArguments
import generators.obj.out.RegionImpl
import kotlin.random.Random

fun main(args: Array<String>) {
    BuildLtspSampels(args[0], 100).build()
}

class BuildLtspSampels(
    private val outputDir: String,
    private val samplesCount: Int
) {
    fun build() {
        val rnd = Random(System.currentTimeMillis())
        for (i in 0..samplesCount - 1) {
            val fieldsCount = rnd.nextInt(3,6)
            val input = RegionImpl().apply {
                addOutBlock("record TEST") {
                    addSub(OutBlockArguments().apply {
                        addSub(ArgumentNode()).apply {
                            addDatatype("int")
                            addVarName("A$i")
                        }
                        addSub(ArgumentNode()).apply {
                            addDatatype("int")
                            addVarName("B")
                        }
                    })
                }
            }
        }
    }
}