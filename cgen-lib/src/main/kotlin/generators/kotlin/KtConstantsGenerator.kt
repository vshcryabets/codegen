package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.addDatatype
import generators.obj.input.addKeyword
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.input.getValue
import generators.obj.out.ConstantNode
import generators.obj.out.FileData
import generators.obj.out.OutBlock
import generators.obj.out.RegionImpl

class KtConstantsGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val dataTypeToString: GetTypeNameUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addSub(OutBlock("object ${desc.name}")).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(ConstantNode().apply {
                            addKeyword("const")
                            addKeyword("val")
                            addVarName(it.name)
                            addKeyword(":")
                            addDatatype(dataTypeToString.typeTo(file, it.getType()))
                            addKeyword("=")
                            val rValue = prepareRightValueUseCase.toRightValue(it.getType(), it.getValue(), file)
                            addSub(rValue)
                        })
                    }
                }
            }
        }
    }
}