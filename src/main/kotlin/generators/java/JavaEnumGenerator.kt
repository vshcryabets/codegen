package generators.java

import ce.defs.DataType
import ce.settings.Project
import generators.obj.AutoincrementInt
import generators.obj.Generator
import generators.obj.input.*
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class JavaEnumGenerator(
    fileGenerator: JavaFileGenerator,
    private val project: Project
) : Generator<ConstantsEnum>(fileGenerator) {

    override fun processBlock(files: List<FileData>, desc: ConstantsEnum): JavaClassData {
        val file = files.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Class file for Kotlin")
        return file.addSub(JavaClassData(desc.name, file)).apply {
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
                val it = leaf as DataField

                if (withRawValues) {
                    autoIncrement.invoke(it)
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