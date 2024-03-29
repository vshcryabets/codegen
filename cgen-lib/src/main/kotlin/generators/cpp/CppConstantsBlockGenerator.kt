package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.*

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
                        addDatatype(Types.typeTo(headerFile, it.type))
                        addVarName(it.name)
                        addKeyword("=")
                        addRValue(Types.toValue(it.type, it.value))
                        addKeyword(";")
                    }
                )
            }
        }
    }
}
