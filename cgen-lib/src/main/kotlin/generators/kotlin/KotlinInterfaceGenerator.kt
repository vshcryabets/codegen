package generators.kotlin

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.*

class KotlinInterfaceGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<InterfaceDescription> {

    override fun invoke(files: List<FileData>, desc: InterfaceDescription) {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        file.addSub(KotlinClassData(desc.name)).let { kclass ->
            addBlockDefaultsUseCase(desc, kclass)
            kclass.addSub(OutBlock("interface ${desc.name}")).apply {
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
        outBlock.addSub(Method(leaf.name).apply {
            val outputList = leaf.findOrNull(OutputList::class.java)

            val simplestResults = outputList?.subs?.filter {
                it is Output
            }?.map {
                it as Output
            }?.sortedBy {
                it.type.getWeight()
            } ?: emptyList()

            val reusableResults = outputList?.subs?.filter {
                it is OutputReusable
            }?.map {
                it as OutputReusable
            }?.sortedBy {
                it.type.getWeight()
            } ?: emptyList()


            if (simplestResults.size == 0) {
               addSub(ResultLeaf(""))
            } else if (simplestResults.size == 1) {
                addSub(ResultLeaf(Types.typeTo(file, simplestResults[0].type)))
            } else {
                throw IllegalStateException("Not supported more then 1 simple result")
            }

            val inputList = leaf.findOrNull(InputList::class.java)

            if (inputList != null && inputList.subs.isNotEmpty()) {
                addSub(InputList()).apply {
                    inputList.subs.forEach {
                        if (it is Input) {
                            addSub(
                                ArgumentNode(
                                    if (it.value.notDefined()) {
                                        "${it.name}: ${Types.typeTo(file, it.type)}"
                                    } else {
                                        "${it.name}: ${Types.typeTo(file, it.type)} = " +
                                                Types.toValue(it.type, it.value)
                                    }
                                )
                            )
                        }
                    }
                    // reusable vars
                    if (reusableResults.size > 0) {
                        if (reusableResults[0].type.getWeight() < DataType.WEIGHT_ARRAY) {
                            throw IllegalStateException("Primitives can't be reusable in kotlin")
                        }
                        reusableResults.forEach {
                            addSub(
                                ArgumentNode(
                                    if (it.value.notDefined()) {
                                        "${it.name}: ${Types.typeTo(file, it.type)}"
                                    } else {
                                        "${it.name}: ${Types.typeTo(file, it.type)} = " +
                                                Types.toValue(it.type, it.value)
                                    }
                                )
                            )
                        }
                    }
                }
            }
        })
    }

}