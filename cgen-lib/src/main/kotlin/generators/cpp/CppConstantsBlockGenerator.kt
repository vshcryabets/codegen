package generators.cpp

import ce.settings.Project
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ConstantDesc
import generators.obj.input.ConstantsBlock
import generators.obj.out.ConstantLeaf
import generators.obj.out.FileData

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsBlock): CppClassData {
        val declaration = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")
        val definition = files.find { it is CppFileData }
            ?: throw java.lang.IllegalStateException("Can't find Definition file for C++")

        return declaration.addSub(CppClassData(desc.name, declaration)).also { classData ->
            addBlockDefaults(desc, classData)
            val autoIncrement = AutoincrementField()
            desc.subs.forEach {
                if (it is ConstantDesc) {
                    autoIncrement.invoke(it)
                    classData.addSub(
                        ConstantLeaf().apply {
                            addKeyword("const")
                            addKeyword(Types.typeTo(declaration, it.type))
                            addVarName(it.name)
                            // "${Types.toValue(classData, it.type, it.value)};"
                        }
                    )
                }
            }
        }
    }
}
