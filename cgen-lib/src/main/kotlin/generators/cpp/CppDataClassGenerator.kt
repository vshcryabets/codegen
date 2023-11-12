package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.*
import generators.obj.out.*

class CppDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(files: List<FileData>, desc: DataClass) {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
//        val definition = files.find { it is CppFileData }
//            ?: throw java.lang.IllegalStateException("Can't find Definition file for C++")

//        definition.findOrCreateSub(ImportsBlock::class.java).addInclude(header.name)
        header.findOrCreateSub(ImportsBlock::class.java)

        val namespace = header.addSub(NamespaceBlock(desc.getParentPath()))

        namespace.addSub(CppClassData(desc.name)).apply {
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
                            addDatatype(Types.typeTo(header, leaf.type))
                            addVarName(leaf.name)
                            if (leaf.value.isDefined()) {
                                addKeyword("=")
                                addRValue(Types.toValue(leaf.type, leaf.value))
                            }
                        })
                    }
                }
            }
        }
    }
}