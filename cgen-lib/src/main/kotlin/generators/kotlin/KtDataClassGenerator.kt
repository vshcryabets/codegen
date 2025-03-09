package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.input.addDatatype
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addOutBlockArguments
import generators.obj.input.addRValue
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.FileData
import generators.obj.out.RegionImpl

class KtDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.firstOrNull()
            ?: throw IllegalStateException("Can't find Main file for Kotlin")
        val dataClassFields = desc.subs
            .filter { it is DataField }
            .map { it as DataField }
        val regularFields = dataClassFields.filter { !it.static }
        val staticFields = dataClassFields.filter { it.static }

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("data class ${desc.name}") {
                addOutBlockArguments {
                    regularFields.forEach { leaf ->
                        addSub(ArgumentNode().apply {
                            addKeyword("val")
                            addVarName(leaf.name)
                            addKeyword(":")
                            addDatatype(Types.typeTo(file, leaf.type))
                            if (leaf.value.isDefined()) {
                                addKeyword("=")
                                addRValue(Types.toValue(leaf.type, leaf.value))
                            }
                        })
                    }
                }
                // companion object
                if (staticFields.isNotEmpty()) {
                    addOutBlock("companion object") {
                        staticFields.forEach { leaf ->
                            addSub(ArgumentNode().apply {
                                addKeyword("const")
                                addKeyword("val")
                                addVarName(leaf.name)
                                addKeyword(":")
                                addDatatype(Types.typeTo(file, leaf.type))
                                if (leaf.value.isDefined()) {
                                    addKeyword("=")
                                    addRValue(Types.toValue(leaf.type, leaf.value))
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}