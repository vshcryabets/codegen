package generators.cpp

import ce.settings.Project
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.input.Namespace
import generators.obj.out.*

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsBlock): CppClassData {
        val declaration = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
//        val definition = files.find { it is CppFileData }
//            ?: throw java.lang.IllegalStateException("Can't find Definition file for C++")

        val namespace = declaration.addSub(NamespaceBlock(desc.getParentPath()))
        return namespace.addSub(CppClassData(desc.name, declaration)).also { classData ->
            addBlockDefaults(desc, classData)
            val autoIncrement = AutoincrementField()
            if (classData.findOrNull(CommentsBlock::class.java) == null) {
                // add default comments block
                classData.addSub(CommentsBlock()).apply {
                    addCommentLine("${fileGenerator.singleComment()} Constants ${desc.name}")
                }
            }
            classData.addSub(OutBlock("")).apply {
                desc.subs.forEach {
                    if (it is ConstantDesc) {
                        autoIncrement.invoke(it)
                        addSub(
                            ConstantLeaf().apply {
                                addKeyword("const")
                                addDatatype(Types.typeTo(declaration, it.type))
                                addVarName(it.name)
                                addKeyword("=")
                                addRValue(Types.toValue(classData, it.type, it.value))
                                addKeyword(";")
                            }
                        )
                        addSeparatorNewLine("")
                    }
                }
            }
        }
    }
}
