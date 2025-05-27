package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataClass
import generators.obj.input.DataField
import generators.obj.input.addCommentLine
import generators.obj.input.addDatatype
import generators.obj.input.addKeyword
import generators.obj.input.addOutBlock
import generators.obj.input.addSub
import generators.obj.input.addVarName
import generators.obj.input.findOrCreateSub
import generators.obj.input.findOrNull
import generators.obj.input.getParentPath
import generators.obj.out.CommentsBlock
import generators.obj.out.FieldNode
import generators.obj.out.FileData
import generators.obj.out.ImportsBlock
import generators.obj.out.NamespaceBlock

class CppDataClassGenerator(
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(files: List<FileData>, desc: DataClass) {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
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
                            addDatatype(Types.typeTo(header, leaf.getType()))
                            addVarName(leaf.name)
                            if (leaf.getValue().isDefined()) {
                                addKeyword("=")
                                addSub(Types.toValue(leaf.getType(), leaf.getValue()))
                            }
                        })
                    }
                }
            }
        }
    }
}