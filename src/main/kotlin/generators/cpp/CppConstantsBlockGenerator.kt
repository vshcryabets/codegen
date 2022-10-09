package generators.cpp

import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsBlock
import generators.obj.out.FileData

class CppConstantsBlockGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsBlock): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        //        val definition = CppClassData(desc.name, header)
        return header.addSub(CppClassData(desc.name, header)).apply {
            desc.classComment.append("Constants ${desc.name}${fileGenerator.newLine()}")
            addBlockDefaults(desc, this)
            val autoIncrement = AutoincrementInt()
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                autoIncrement.invoke(it)

                classDefinition.append("const ")
                    .append(Types.typeTo(header, it.type))
                    .append(" ")
                    .append(it.name)
                    .append(" = ${Types.toValue(header, it.type, it.value)};")
                    .append(fileGenerator.newLine())
            }
        }
    }
}