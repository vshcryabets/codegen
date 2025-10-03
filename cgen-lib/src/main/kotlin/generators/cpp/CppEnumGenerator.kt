package generators.cpp

import ce.defs.DataType
import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.ConstantsEnum
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addCommentLine
import generators.obj.abstractSyntaxTree.addEnumLeaf
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.RegionImpl

class CppEnumGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
) : TransformBlockUseCase<ConstantsEnum> {

    override fun invoke(files: List<FileData>, desc: ConstantsEnum) {
        val headerFile = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        val namespace = headerFile.addSub(NamespaceBlock(desc.getParentPath()))
        val region = namespace.addSub(RegionImpl(desc.name))
        val withRawValues = desc.defaultDataType != DataType.VOID
        val autoIncrement = AutoincrementField()

        addBlockDefaultsUseCase(desc, region)
        if (region.findOrNull(CommentsBlock::class.java) == null) {
            // add default comments block
            region.addSub(CommentsBlock()).apply {
                addCommentLine("Enum ${desc.name}")
            }
        }

        region.addOutBlock("enum ${desc.name}") {
            desc.subs.forEach { leaf ->
                if (leaf is DataField) {
                    val it = leaf
                    if (withRawValues) {
                        autoIncrement(it)
                        val rValue = prepareRightValueUseCase.toRightValue(
                            dataField = it,
                            fileData = headerFile
                        )
                        addEnumLeaf("").apply {
                            addVarName(it.name)
                            addKeyword("=")
                            addSub(rValue)
                        }
                    } else {
                        addEnumLeaf(it.name)
                    }
                }
            }
        }
    }
}