package generators.java

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
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.input.getValue
import generators.obj.out.FieldNode
import generators.obj.out.FileData
import generators.obj.out.RegionImpl

class JavaEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsEnum) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl(desc.name)).also { region ->
            addBlockDefaultsUseCase(desc, region)
            region.addOutBlock("enum ${desc.name}") {
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        val it = leaf

                        if (withRawValues) {
                            autoIncrement(it)
                            addEnumLeaf("${it.name}(${Types.toValue(it.getType(), it.getValue())})")
                        } else {
                            addEnumLeaf(it.name)
                        }
                    }
                }
                if (withRawValues) {
                    addSub(FieldNode()).apply {
                        addKeyword("final")
                        addDatatype(generators.java.Types.typeTo(file, desc.defaultDataType))
                        addVarName("value")
                    }
                }
            }
        }
    }
}