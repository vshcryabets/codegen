package generators.kotlin

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.Input
import generators.obj.input.InputList
import generators.obj.input.InterfaceDescription
import generators.obj.input.Method
import generators.obj.input.Output
import generators.obj.input.OutputList
import generators.obj.input.OutputReusable
import generators.obj.input.addSub
import generators.obj.input.findOrNull
import generators.obj.input.getValue
import generators.obj.out.ArgumentNode
import generators.obj.out.FileData
import generators.obj.out.OutBlock
import generators.obj.out.ResultLeaf

class KotlinInterfaceGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val dataTypeToString: GetTypeNameUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
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
                it.getType().getWeight()
            } ?: emptyList()

            val reusableResults = outputList?.subs?.filter {
                it is OutputReusable
            }?.map {
                it as OutputReusable
            }?.sortedBy {
                it.getType().getWeight()
            } ?: emptyList()


            if (simplestResults.size == 0) {
                addSub(ResultLeaf(""))
            } else if (simplestResults.size == 1) {
                addSub(ResultLeaf(dataTypeToString.typeTo(file, simplestResults[0].getType())))
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
                                    if (!it.getValue().isDefined()) {
                                        "${it.name}: ${dataTypeToString.typeTo(file, it.getType())}"
                                    } else {
                                        val rValue = prepareRightValueUseCase.toRightValue(it.getType(), it.getValue(), file)
                                        "${it.name}: ${dataTypeToString.typeTo(file, it.getType())} = " + rValue
                                    }
                                )
                            )
                        }
                    }
                    // reusable vars
                    if (reusableResults.size > 0) {
                        if (reusableResults[0].getType().getWeight() < DataType.WEIGHT_ARRAY) {
                            throw IllegalStateException("Primitives can't be reusable in kotlin")
                        }
                        reusableResults.forEach {
                            addSub(
                                ArgumentNode(
                                    if (!it.getValue().isDefined()) {
                                        "${it.name}: ${dataTypeToString.typeTo(file, it.getType())}"
                                    } else {
                                        val rValue = prepareRightValueUseCase.toRightValue(it.getType(), it.getValue(), file)
                                        "${it.name}: ${dataTypeToString.typeTo(file, it.getType())} = " +
                                                rValue
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