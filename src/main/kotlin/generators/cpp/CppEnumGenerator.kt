package generators.cpp

import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.ClassField
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class CppEnumGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, CppClassData>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsEnum): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        //        val definition = CppClassData(desc.name, header)
        return header.addSub(CppClassData(desc.name, header)).apply {
            addBlockDefaults(desc, this)

            classDefinition.append("enum ${desc.name} {").append(fileGenerator.newLine())
            val autoIncrement = AutoincrementInt()
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                autoIncrement.invoke(it)

                putTabs(classDefinition, 1)
                classDefinition.append(it.name);
                classDefinition.append(" = ${Types.toValue(header, it.type, it.value)},")
                classDefinition.append('\n')
            }
            appendClassDefinition(this, "};");

        }
    }

}