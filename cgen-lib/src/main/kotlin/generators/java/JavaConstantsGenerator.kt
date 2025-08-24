package generators.java

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.kotlin.PrepareRightValueUseCase
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

class JavaConstantsGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Main file for Java")
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addSub(OutBlock("public class ${desc.name}")).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(FieldNode().apply {
                            addKeyword("public")
                            addKeyword("static")
                            addKeyword("final")
                            addDatatype(Types.typeTo(file, it.getType()))
                            addVarName(it.name)
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