package generators.java

import ce.settings.Project
import generators.obj.AutoincrementField
import generators.obj.FileGenerator
import generators.obj.Generator
import generators.obj.input.DataField
import generators.obj.input.ConstantsBlock
import generators.obj.out.BlockStart
import generators.obj.out.FileData

class JavaConstantsGenerator(
    fileGenerator : FileGenerator,
    private val project: Project
) : Generator<ConstantsBlock>(fileGenerator) {

    override fun processBlock(blockFiles: List<FileData>, desc: ConstantsBlock): JavaClassData {
        val file = blockFiles.find { it is FileData }
            ?: throw java.lang.IllegalStateException("Can't find Main file for Kotlin")

        return file.addSub(JavaClassData(desc.name, file)).apply {
            addBlockDefaults(desc, this)
            subs.add(BlockStart("public class ${desc.name} {", this))
            val autoIncrement = AutoincrementField()
            desc.subs.forEach { leaf ->
                val it = leaf as DataField
                autoIncrement.invoke(it)

                classDefinition.append(fileGenerator.tabSpace);
                classDefinition.append("public static final ${Types.typeTo(file, it.type)} ");
                classDefinition.append(it.name);
                classDefinition.append(" = ${Types.toValue(this, it.type, it.value)};")
                classDefinition.append(fileGenerator.newLine())
            }
            appendNotEmptyWithNewLine("}", classDefinition)
        }
    }
}