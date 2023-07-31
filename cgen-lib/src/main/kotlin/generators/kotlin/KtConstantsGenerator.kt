package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.out.*

class KtConstantsGenerator(
    fileGenerator : FileGenerator,
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")
        val autoIncrement = AutoincrementField()

        file.addSub(KotlinClassData(desc.name)).also { classData ->
            addBlockDefaultsUseCase(desc, classData)
            classData.addSub(OutBlock("object ${desc.name}")).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(ConstantLeaf {
                            addKeyword("const")
                            addKeyword("val")
                            addVarName(it.name)
                            addKeyword(":")
                            addDatatype(Types.typeTo(file, it.type))
                            addKeyword("=")
                            addRValue(Types.toValue(classData, it.type, it.value))
                            addSeparatorNewLine("")
                        })
                    }
                }
            }
        }
    }
}