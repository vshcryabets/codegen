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
        val classData = namespace.addSub(RegionImpl(desc.name))
        addBlockDefaultsUseCase(desc, classData)
        val autoIncrement = AutoincrementField()

        if (classData.findOrNull(CommentsBlock::class.java) == null) {
            // add default comments block
            classData.addSub(CommentsBlock()).apply {
                addCommentLine("Constants ${desc.name}")
            }
        }
        val outBlock = classData

        desc.subs.forEach {
            if (it is ConstantDesc) {
                autoIncrement.invoke(it)
                outBlock.addSub(
                    ConstantLeaf().apply {
                        addKeyword("const")
                        addDatatype(Types.typeTo(headerFile, it.type))
                        addVarName(it.name)
                        addKeyword("=")
                        addRValue(Types.toValue(it.type, it.value))
                        addKeyword(";")
                    }
                )
                outBlock.addSeparatorNewLine("")
            }
        }
    }
}
