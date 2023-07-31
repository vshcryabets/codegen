package generators.cpp

import ce.domain.usecase.add.AddRegionDefaultsUseCase
import ce.formatters.CodeStyleRepo
import generators.obj.TransformBlockUseCase
import generators.obj.input.DataField
import generators.obj.input.DataClass
import generators.obj.out.*

class CppDataClassGenerator(
    private val codestyleRepo: CodeStyleRepo,
    private val addBlockDefaultsUseCase: AddRegionDefaultsUseCase,
) : TransformBlockUseCase<DataClass> {

    override fun invoke(files: List<FileData>, desc: DataClass) {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
        val definition = files.find { it is CppFileData }
            ?: throw java.lang.IllegalStateException("Can't find Definition file for C++")

        definition.findOrCreateSub(ImportsBlock::class.java).addInclude(header.name)

        val namespace = header.addSub(NamespaceBlock(desc.getParentPath()))

        namespace.addSub(CppClassData(desc.name)).apply {
            addBlockDefaultsUseCase(desc, this)
            if (findOrNull(CommentsBlock::class.java) == null) {
                // add default comments block
                addSub(CommentsBlock()).apply {
                    addCommentLine("${codestyleRepo.singleComment()} Data class ${desc.name}")
                }
            }
            addOutBlock("struct ${desc.name}") {
                addSub(NlSeparator())
                var addNewLine = false
                desc.subs.forEach { leaf ->
                    if (leaf is DataField) {
                        if (addNewLine) {
                            addSeparatorNewLine(";")
                        }
                        addDataField("${Types.typeTo(header, leaf.type)} ${leaf.name}", leaf.type)
                        addNewLine = true
                    }
                }
            }
        }
    }
}