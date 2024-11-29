package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.kotlin.Types
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.ConstantNode
import generators.obj.out.FileData
import generators.obj.out.OutBlock

class JavaConstantsGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Main file for Java")
        val autoIncrement = AutoincrementField()

        file.addSub(JavaClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
            addSub(OutBlock("public class ${desc.name}")).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(ConstantNode().apply {
                            addDatatype(Types.typeTo(file, it.type))
                            addVarName(it.name)
                            addKeyword("=")
                            addRValue(Types.toValue(it.type, it.value))
                        })
                    }
                }
            }
        }
    }
}