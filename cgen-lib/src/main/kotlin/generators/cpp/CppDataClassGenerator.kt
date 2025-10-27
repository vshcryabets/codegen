package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.DataClass
import generators.obj.abstractSyntaxTree.DataField
import generators.obj.abstractSyntaxTree.addCommentLine
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addOutBlock
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.abstractSyntaxTree.findOrCreateSub
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.ImportsBlock
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.RegionImpl

class CppDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
    private val dataTypeToString: GetTypeNameUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(blockFiles: List<FileData>, desc: DataClass) {
        val header = blockFiles.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
        header.findOrCreateSub(ImportsBlock::class.java)
        val imports = header.findOrCreateSub(ImportsBlock::class.java)

        val namespace = header.addSub(NamespaceBlock(desc.getParentPath()))

        namespace.addSub(RegionImpl(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
            if (findOrNull(CommentsBlock::class.java) == null) {
                // add default comments block
                addSub(CommentsBlock()).apply {
                    addCommentLine("Data class ${desc.name}")
                }
            }
            addOutBlock("struct ${desc.name}") {
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        addSub(FieldNode().apply {
                            addDatatype(
                                dataTypeToString.typeTo(
                                    importsBlock = imports,
                                    type = leaf.getType()
                                )
                            )
                            addVarName(leaf.name)
                            if (leaf.getValue().isDefined()) {
                                addKeyword("=")
                                val rValue = prepareRightValueUseCase.toRightValue(
                                    dataField = leaf,
                                    importsBlock = imports
                                )
                                addSub(rValue)
                            }
                        })
                    }
                }
            }
        }
    }
}