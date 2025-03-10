package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataClass
import generators.obj.input.addSub
import generators.obj.out.FileData
import generators.obj.out.RegionImpl
import generators.obj.input.*
import generators.obj.out.ArgumentNode

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
                                addDatatype(Types.typeTo(file, leaf.type))
                                addVarName(leaf.name)
                            })
                        }
                    }
                }
            }
        }
    }
}
