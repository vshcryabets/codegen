package generators.cpp

import ce.settings.Project
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataField
import generators.obj.input.DataClass
import generators.obj.out.*

class CppDataClassGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<DataClass>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: DataClass): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
        val definition = files.find { it is CppFileData }
            ?: throw java.lang.IllegalStateException("Can't find Definition file for C++")

        definition.findOrCreateSub(ImportsBlock::class.java).addInclude(header.name)

        val namespace = header.addSub(NamespaceBlock(desc.getParentPath()))

        return namespace.addSub(CppClassData(desc.name, header)).apply {
            addBlockDefaults(desc, this)
            if (findOrNull(CommentsBlock::class.java) == null) {
                // add default comments block
                addSub(CommentsBlock()).apply {
                    addCommentLine("${fileGenerator.singleComment()} Data class ${desc.name}")
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