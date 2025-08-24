package generators.java

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
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.RegionImpl

class JavaEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsEnum) {
        val file = blockFiles.firstOrNull()
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()

        file.addSub(RegionImpl(desc.name)).also { region ->
            addBlockDefaultsUseCase(desc, region)
            region.addOutBlock("enum ${desc.name}") {
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        val it = leaf

                        if (withRawValues) {
                            autoIncrement(it)
                            addEnumLeaf("${it.name}(${Types.toValue(it.getType(), it.getValue())})")
                        } else {
                            addEnumLeaf(it.name)
                        }
                    }
                }
                if (withRawValues) {
                    addSub(FieldNode()).apply {
                        addKeyword("final")
                        addDatatype(generators.java.Types.typeTo(file, desc.defaultDataType))
                        addVarName("value")
                    }
                }
            }
        }
    }
}