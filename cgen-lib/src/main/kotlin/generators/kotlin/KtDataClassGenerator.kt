package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.FileData
import generators.obj.out.RegionImpl

class KtDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.find { it is FileData }
            ?: throw IllegalStateException("Can't find Main file for Kotlin")

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("data class ${desc.name}") {
                addOutBlockArguments {
                    desc.subs.forEach { leaf ->
                        if (leaf is DataField) {
                            addSub(DataField().apply {
                                addKeyword("val")
                                addVarName(leaf.name)
                                addKeyword(":")
                                addDatatype(Types.typeTo(file, leaf.type))
                                addKeyword("=")
                                addRValue(Types.toValue(leaf.type, leaf.value))
                            })

                            addDataField(
                                if (leaf.value.isDefined())
                                    "val ${leaf.name}: ${Types.typeTo(file, leaf.type)} = ${Types.toValue(leaf.type, leaf.value)}"
                                else
                                    "val ${leaf.name}: ${Types.typeTo(file, leaf.type)}",
                                leaf.type)
                        }
                    }
                }
            }
        }
    }
}