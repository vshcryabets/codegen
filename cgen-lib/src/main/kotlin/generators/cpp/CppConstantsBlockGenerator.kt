package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.addCommentLine
import generators.obj.input.addDatatype
import generators.obj.input.addKeyword
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.input.findOrNull
import generators.obj.input.getParentPath
import generators.obj.input.getValue
import generators.obj.out.CommentsBlock
import generators.obj.out.ConstantNode
import generators.obj.out.FileData
import generators.obj.out.NamespaceBlock
import generators.obj.out.RegionImpl

class CppConstantsBlockGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
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
                    ConstantNode().apply {
                        addKeyword("const")
                        addDatatype(Types.typeTo(headerFile, it.getType()))
                        addVarName(it.name)
                        addKeyword("=")
                        addSub(Types.toValue(it.getType(), it.getValue()))
                        addKeyword(";")
                    }
                )
            }
        }
    }
}
