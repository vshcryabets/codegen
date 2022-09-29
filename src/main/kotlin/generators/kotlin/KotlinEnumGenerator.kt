package generators.kotlin

import ce.defs.DataType
import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.Generator
import generators.obj.input.*
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class KotlinEnumGenerator(
    fileGenerator: KotlinFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum, KotlinClassData>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsEnum): KotlinClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Header file for Kotlin")

        //        val definition = CppClassData(desc.name, header)
        return file.addSub(KotlinClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            val withRawValues = desc.defaultDataType != DataType.VOID
            subs.add(BlockStart("enum class ${desc.name}", this))
            if (!withRawValues) {
                classDefinition.append(" {")
                    .append(fileGenerator.newLine())
            } else {
                classDefinition.append("(val rawValue : ${Types.typeTo(file, desc.defaultDataType)}) {")
                    .append(fileGenerator.newLine())
            }

            val autoIncrement = AutoincrementInt()
            var needToAddComa = false
            desc.subs.forEach { leaf ->
                val it = leaf as ClassField
                autoIncrement.invoke(it)

                if (withRawValues) {
                    putTabs(classDefinition, 1)
                    classDefinition
                        .append(it.name)
                        .append("(${Types.toValue(this, it.type, it.value)}),")
                        .append(fileGenerator.newLine())
                } else {
                    if (needToAddComa) {
                        classDefinition.append(", ")
                    }
                    classDefinition.append(it.name)
                    needToAddComa = true
                }
            }
            if (!withRawValues) {
                classDefinition.append(fileGenerator.newLine())
                    .append(fileGenerator.newLine())
            }
            appendClassDefinition(this, "}");
        }
    }
}