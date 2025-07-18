package generators.kotlin

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantsEnum
import generators.obj.input.DataField
import generators.obj.input.addDatatype
import generators.obj.input.addEnumLeaf
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addOutBlockArguments
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.Arguments
import generators.obj.out.FileData
import generators.obj.out.RegionImpl

class KotlinEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val dataTypeToString: GetTypeNameUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsEnum) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl(desc.name)).also { region ->
            addBlockDefaultsUseCase(desc, region)
            region.addOutBlock("enum class ${desc.name}") {
                if (withRawValues) {
                    addOutBlockArguments {
                        addSub(ArgumentNode().apply {
                            addKeyword("val")
                            addVarName("rawValue")
                            addKeyword(":")
                            addDatatype(dataTypeToString.typeTo(file, desc.defaultDataType))
//                            addKeyword("=")
//                            addRValue(Types.toValue(desc.type, desc.value))
                        })
//                        addDataField("val rawValue : ${Types.typeTo(file, desc.defaultDataType)}", desc.defaultDataType)
                    }
                }
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        val it = leaf

                        if (withRawValues) {
                            autoIncrement(it)
                            val rValue = prepareRightValueUseCase.toRightValue(
                                dataField = it,
                                fileData = file
                            )
                            addEnumLeaf(it.name).apply {
                                addSub(Arguments()).apply {
                                    addSub(rValue)
                                }
                            }
                        } else {
                            addEnumLeaf(it.name)
                        }
                    }
                }
            }
        }
    }
}