package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.*

class KtConstantsGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addSub(OutBlock("object ${desc.name}")).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(ConstantLeaf().apply {
                            addKeyword("const")
                            addKeyword("val")
                            addVarName(it.name)
                            addKeyword(":")
                            addDatatype(Types.typeTo(file, it.type))
                            addKeyword("=")
                            addRValue(Types.toValue(it.type, it.value))
                        })
                    }
                }
            }
        }
    }
}