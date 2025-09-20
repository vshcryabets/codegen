package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.kotlin.PrepareRightValueUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.abstractSyntaxTree.ConstantDesc
import generators.obj.abstractSyntaxTree.ConstantsBlock
import generators.obj.abstractSyntaxTree.addCommentLine
import generators.obj.abstractSyntaxTree.addDatatype
import generators.obj.abstractSyntaxTree.addKeyword
import generators.obj.abstractSyntaxTree.addSub
import generators.obj.abstractSyntaxTree.addVarName
import generators.obj.abstractSyntaxTree.findOrNull
import generators.obj.abstractSyntaxTree.getParentPath
import generators.obj.syntaxParseTree.CommentsBlock
import generators.obj.syntaxParseTree.FieldNode
import generators.obj.syntaxParseTree.FileData
import generators.obj.syntaxParseTree.NamespaceBlock
import generators.obj.syntaxParseTree.RegionImpl

class CppConstantsBlockGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
    private val prepareRightValueUseCase: PrepareRightValueUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val headerFile = blockFiles.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        val namespace = headerFile.addSub(NamespaceBlock(desc.getParentPath()))
        val outBlock = namespace.addSub(RegionImpl(desc.name))
        addBlockDefaultsUseCase(desc, outBlock)
        val autoIncrement = AutoincrementField()

        if (outBlock.findOrNull(CommentsBlock::class.java) == null) {
            // add default comments block
            outBlock.addSub(CommentsBlock()).apply {
                addCommentLine("Constants ${desc.name}")
            }
        }

        desc.subs.forEach {
            if (it is ConstantDesc) {
                autoIncrement.invoke(it)
                outBlock.addSub(
                    FieldNode().apply {
                        addKeyword("const")
                        addDatatype(Types.typeTo(headerFile, it.getType()))
                        addVarName(it.name)
                        addKeyword("=")
                        val rValue = prepareRightValueUseCase.toRightValue(it.getType(), it.getValue(), headerFile)
                        addSub(rValue)
                    }
                )
            }
        }
    }
}
