package generators.kotlin

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addOutBlockArguments
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.RegionImpl

class KtDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val dataTypeToString: GetTypeNameUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val file = blockFiles.firstOrNull()
            ?: throw IllegalStateException("Can't find Main file for Kotlin")
        val dataClassFields = desc.subs
            .filter { it is DataField }
            .map { it as DataField }
        val regularFields = dataClassFields.filter { !it.static }
        val staticFields = dataClassFields.filter { it.static }

        file.addSub(RegionImpl()).apply {
            addBlockDefaultsUseCase(desc, this)
            addOutBlock("data class ${desc.name}") {
                addOutBlockArguments {
                    regularFields.forEach { leaf ->
                        addSub(ArgumentNode().apply {
                            addKeyword("val")
                            addVarName(leaf.name)
                            addKeyword(":")
                            addDatatype(dataTypeToString.typeTo(file, leaf.getType()))
                            if (leaf.getValue().isDefined()) {
                                addKeyword("=")
                                val rValue = prepareRightValueUseCase.toRightValue(leaf.getType(), leaf.getValue(), file)
                                addSub(rValue)
                            }
                        })
                    }
                }
                // companion object
                if (staticFields.isNotEmpty()) {
                    addOutBlock("companion object") {
                        staticFields.forEach { leaf ->
                            addSub(FieldNode().apply {
                                val rvalue = leaf.getValue()
                                if (rvalue.isDefined()) {
                                    if (!rvalue.isComplex)
                                        addKeyword("const")
                                    addKeyword("val")
                                    addVarName(leaf.name)
                                    addKeyword(":")
                                    addDatatype(dataTypeToString.typeTo(file, leaf.getType()))
                                    addKeyword("=")
                                    val rValue = prepareRightValueUseCase.toRightValue(leaf.getType(), leaf.getValue(), file)
                                    addSub(rValue)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}