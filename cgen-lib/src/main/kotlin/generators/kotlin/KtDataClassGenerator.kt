package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.ArgumentNode
import generators.obj.out.FileData
import generators.obj.out.RegionImpl

class KtDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.firstOrNull()
            ?: throw IllegalStateException("Can't find Main file for Kotlin")

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("data class ${desc.name}") {
                addOutBlockArguments {
                    desc.subs.forEach { leaf ->
                        if (leaf is DataField) {
                            addSub(ArgumentNode().apply {
                                addKeyword("val")
                                addVarName(leaf.name)
                                addKeyword(":")
                                addDatatype(Types.typeTo(file, leaf.type))
                                addKeyword("=")
                                addRValue(Types.toValue(leaf.type, leaf.value))
                            })
                        }
                    }
                }
            }
        }
    }
}