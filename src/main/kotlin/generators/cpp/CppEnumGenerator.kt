package generators.cpp

import ce.defs.DataType
import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataField
import generators.obj.input.ConstantsEnum
import generators.obj.out.FileData

class CppEnumGenerator(
    fileGenerator: FileGenerator,
    private val project: Project
) : Generator<ConstantsEnum>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsEnum): CppClassData {
        val header = files.find { it is CppHeaderFile }
            ?: throw java.lang.IllegalStateException("Can't find Header file for C++")

        //        val definition = CppClassData(desc.name, header)
        return header.addSub(CppClassData(desc.name, header)).apply {
            addBlockDefaults(desc, this)
            val withRawValues = desc.defaultDataType != DataType.VOID

            classDefinition.append("enum ${desc.name} {").append(fileGenerator.newLine())
            val autoIncrement = AutoincrementInt()
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
                putTabs(classDefinition, 1)

                if (withRawValues) {
                    autoIncrement.invoke(it)
                    classDefinition.append(it.name);
                    classDefinition.append(" = ${Types.toValue(this, it.type, it.value)},")
                    classDefinition.append(fileGenerator.newLine())
                } else {
                    classDefinition.append("${it.name},${fileGenerator.newLine()}");
                }
            }
            appendClassDefinition(this, "};");

        }
    }

}