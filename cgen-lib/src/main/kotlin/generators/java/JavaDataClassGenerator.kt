package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.input.addDatatype
import generators.obj.input.addOutBlock
import generators.obj.input.addOutBlockArguments
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.out.ArgumentNode
import generators.obj.out.FileData
import generators.obj.out.RegionImpl

class JavaDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Main file for $desc")

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("public record ${desc.name}") {
                addOutBlockArguments {
                    desc.subs.forEach { leaf ->
                        if (leaf is DataField) {
                            addSub(ArgumentNode().apply {
                                addDatatype(Types.typeTo(file, leaf.getType()))
                                addVarName(leaf.name)
                            })
                        }
                    }
                }
            }
        }
    }
}
