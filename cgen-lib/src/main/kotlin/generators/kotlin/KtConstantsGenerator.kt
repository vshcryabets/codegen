package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.ConstantDesc
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.OutBlock
import generators.obj.syntaxParseTree.RegionImpl

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
                        addSub(FieldNode().apply {
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