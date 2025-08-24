package generators.kotlin

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.ConstantsEnum
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addEnumLeaf
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addOutBlockArguments
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.ArgumentNode
import generators.obj.syntaxParseTree.Arguments
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.RegionImpl

class KotlinEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val dataTypeToString: GetTypeNameUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsEnum) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl(desc.name)).also { region ->
            addBlockDefaultsUseCase(desc, region)
            region.addOutBlock("enum class ${desc.name}") {
                if (withRawValues) {
                    addOutBlockArguments {
                        addSub(ArgumentNode().apply {
                            addKeyword("val")
                            addVarName("rawValue")
                            addKeyword(":")
                            addDatatype(dataTypeToString.typeTo(file, desc.defaultDataType))
//                            addKeyword("=")
//                            addRValue(Types.toValue(desc.type, desc.value))
                        })
//                        addDataField("val rawValue : ${Types.typeTo(file, desc.defaultDataType)}", desc.defaultDataType)
                    }
                }
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        val it = leaf

                        if (withRawValues) {
                            autoIncrement(it)
                            val rValue = prepareRightValueUseCase.toRightValue(
                                dataField = it,
                                fileData = file
                            )
                            addEnumLeaf(it.name).apply {
                                addSub(Arguments()).apply {
                                    addSub(rValue)
                                }
                            }
                        } else {
                            addEnumLeaf(it.name)
                        }
                    }
                }
            }
        }
    }
}