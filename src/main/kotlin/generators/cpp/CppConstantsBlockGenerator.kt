package generators.cpp

import ce.settings.Project
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataField
import generators.obj.input.ConstantsBlock
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

        return declaration.addSub(CppClassData(desc.name, declaration)).apply {
//            desc.classComment.append("Constants ${desc.name}${fileGenerator.newLine()}")
            addBlockDefaults(desc, this)
            val autoIncrement = AutoincrementField()
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
                autoIncrement.invoke(it)

                classDefinition.append("const ")
                    .append(Types.typeTo(declaration, it.type))
                    .append(" ")
                    .append(it.name)
                    .append(" = ${Types.toValue(this, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
    }
}