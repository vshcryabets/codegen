package generators.kotlin

import ce.settings.Project
import generators.obj.Generator
import generators.obj.input.*
import generators.obj.out.*

class KotlinInterfaceGenerator(
    fileGenerator: KotlinFileGenerator,
    private val project: Project
) : Generator<InterfaceDescription>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: InterfaceDescription): KotlinClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        return file.addSub(KotlinClassData(desc.name, file)).let { kclass ->
            addBlockDefaults(desc, kclass)
            kclass.addSub(OutBlock("interface ${desc.name}", kclass)).apply {
                desc.subs.forEach { leaf ->
                    if (leaf is Method) {
                        addMethod(this, leaf, file, kclass)
                    }
                }
            }
            kclass
        }
    }

    private fun addMethod(outBlock: OutBlock, leaf: Method, file: FileData, itf: KotlinClassData) {
        outBlock.addSub(Method("fun ${leaf.name}").apply {
            val outputList = leaf.findOrNull(OutputList::class.java)
            val sl = outputList?.subs?.filter {
                it is Output
            }?.map {
                it as Output
            }?.sortedBy {
                it.type.getWeight()
            } ?: emptyList()

            if (sl.size == 1) {
                addSub(ResultLeaf(" : ${Types.typeTo(file, sl[0].type)}"))
            } else {
                throw java.lang.IllegalStateException("Not uspported more then 1 result")
            }

            val inputList = leaf.findOrNull(InputList::class.java)
            if (inputList != null && inputList.subs.isNotEmpty()) {
                var needToAddComa= false
                addSub(InputList()).apply {
                    inputList.subs.forEach {
                        if (it is Input) {
                            if (needToAddComa) addSub(Separator(", "))
                            addSub(
                                ArgumentLeaf(
                                    if (it.value.notDefined()) {
                                        "${it.name} : ${Types.typeTo(file, it.type)}"
                                    } else {
                                        "${it.name} : ${Types.typeTo(file, it.type)} = " +
                                                "${Types.toValue(itf, it.type, it.value)}"
                                    }))
                            needToAddComa = true
                        }
                    }
                }
            }
        })
    }

}