package generators.kotlin

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.*

class KotlinEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(files: List<FileData>, desc: ConstantsEnum) {
        val file = files.firstOrNull()
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
                            addDatatype(Types.typeTo(file, desc.defaultDataType))
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
                            addEnumLeaf("${it.name}(${Types.toValue(it.type, it.value)})")
                        } else {
                            addEnumLeaf("${it.name}")
                        }
                    }
                }
            }
        }
    }
}