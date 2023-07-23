package generators.cpp

import ce.domain.usecase.add.AddBlockDefaultsUseCase
import generators.obj.AutoincrementField
import generators.obj.TransformBlockUseCase
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.out.*

class CppConstantsBlockGenerator(
    private val addBlockDefaultsUseCase: AddBlockDefaultsUseCase,
) : TransformBlockUseCase<ConstantsBlock> {

    override fun invoke(blockFiles: List<FileData>, desc: ConstantsBlock) {
        val declaration = blockFiles.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        val namespace = declaration.addSub(NamespaceBlock(desc.getParentPath()))
        val classData = CppClassData(desc.name, declaration)
        namespace.addSub(classData)

        addBlockDefaultsUseCase(desc, classData)

        val autoIncrement = AutoincrementField()

        if (classData.findOrNull(CommentsBlock::class.java) == null) {
            // add default comments block
            classData.addSub(CommentsBlock()).apply {
                addCommentLine("Constants ${desc.name}")
            }
        }

        val outBlock = classData.addSub(OutBlock(""))

        desc.subs.forEach {
            if (it is ConstantDesc) {
                autoIncrement.invoke(it)
                outBlock.addSub(
                    ConstantLeaf().apply {
                        addKeyword("const")
                        addDatatype(Types.typeTo(declaration, it.type))
                        addVarName(it.name)
                        addKeyword("=")
                        addRValue(Types.toValue(classData, it.type, it.value))
                        addKeyword(";")
                    }
                )
                outBlock.addSeparatorNewLine("")
            }
        }
    }
}
